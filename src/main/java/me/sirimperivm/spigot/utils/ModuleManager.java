package me.sirimperivm.spigot.utils;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.entities.CustomDrop;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.enchants.Enchants;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("all")
public class ModuleManager {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private Enchants enchants;

    public ModuleManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        enchants = plugin.getEnchants();
    }

    public void createHelp(CommandSender s, String helpTarget, int page) {
        int visualizedPage = page;
        page--;

        List<String> totalLines = configManager.getMessages().getStringList("helps-creator." + helpTarget + ".lines");
        int commandsPerPage = configManager.getMessages().getInt("helps-creator." + helpTarget + ".max-lines-per-page");
        int startIndex = page*commandsPerPage;
        int totalCommands = totalLines.size();
        int endIndex = Math.min((page+1) * commandsPerPage, totalCommands);

        if (visualizedPage <= 0 || visualizedPage > (int) Math.floor((double) totalCommands/commandsPerPage)+1) {
            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"page-not-found")
                    .replace("{page}", String.valueOf(visualizedPage))
            );
            return;
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".header"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".title"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".spacer"));

        for (int i=startIndex; i<endIndex; i++) {
            String line = totalLines.get(i);
            if (line != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String commandName = parts[0].trim();
                    String commandDescription = parts[1].trim();
                    s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".line-format")
                            .replace("{command-name}", colors.translateString(commandName))
                            .replace("{command-description}", colors.translateString(commandDescription))
                    );
                }
            }
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".spacer"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".page-format")
                .replace("{currentpage}", String.valueOf(visualizedPage))
        );
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".footer"));
    }

    public ItemStack getRandomDrop(String type, String target) {
        List<CustomDrop> drops = new ArrayList<>();
        for (String dropObj : configManager.getTables().getConfigurationSection(type + "." + target + ".custom-drops").getKeys(false)) {
            String path = type + "." + target + ".custom-drops." + dropObj;

            Material material = Material.getMaterial(configManager.getTables().getString(path + ".type"));
            int minAmount = configManager.getTables().getInt(path + ".min-amount");
            int maxAmount = configManager.getTables().getInt(path + ".max-amount");
            double chance = configManager.getTables().getDouble(path + ".chance");

            CustomDrop drop = new CustomDrop(dropObj, material, minAmount, maxAmount, chance);
            drops.add(drop);
        }

        double maxChance = 0;
        for (CustomDrop drop : drops) {
            maxChance += drop.getChance();
        }

        double randomValue = Math.random() * 100;
        double cumulativeChance = 0;

        for (CustomDrop drop : drops) {
            cumulativeChance += drop.getChance();
            if (randomValue <= cumulativeChance) {
                int amount = getRandomAmount(drop.getMinAmount(), drop.getMaxAmount());

                String path = type + "." + target + ".custom-drops." + drop.getId();

                ItemStack item = new ItemStack(drop.getType(), amount);
                ItemMeta meta = item.getItemMeta();
                String displayname = configManager.getTables().getString(path + ".display");
                if (!displayname.equals("null"))
                    meta.setDisplayName(colors.translateString(displayname));
                meta.setLore(configManager.getTranslatedList(configManager.getTables(), path + ".lore"));
                for (String flag : configManager.getTables().getStringList(path + ".flags"))
                    meta.addItemFlags(ItemFlag.valueOf(flag));
                item.setItemMeta(meta);
                for (String enchantString : configManager.getTables().getStringList(path + ".enchants")) {
                    String[] splitter = enchantString.split(":");
                    String enchantName = splitter[0];
                    int enchantLevel = 0;
                    try {
                        enchantLevel = Integer.parseInt(splitter[1]);
                    } catch (NumberFormatException e) {
                        log.fail("Impossibile aggiungere l'incantesimo a quest'oggetto; la stringa del livello non è un numero.");
                        continue;
                    }
                    enchantLevel--;
                    if (enchantLevel < 0) {
                        log.fail("Impossibile aggiungere l'incantesimo a quest'oggetto; il livello è negativo.");
                        continue;
                    }

                    item.addEnchantment(enchants.getEnchant(enchantName), enchantLevel);
                }

                return item;
            }
        }

        return null;
    }

    private int getRandomAmount(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max-min) + 1) + min;
    }

    public int searchDuplicates(String type, String target) {
        int duplicates = 0;
        for (String duplicateObj : configManager.getTables().getConfigurationSection(type + "." + target).getKeys(false)) {
            duplicates++;
        }
        return duplicates;
    }

    public double searchMaxChance(String type, String target) {
        double chance = 0;
        for (String dropObj : configManager.getTables().getConfigurationSection(type + "." + target + ".custom-drops").getKeys(false)) {
            chance += configManager.getTables().getDouble(type + "." + target + ".custom-drops." + dropObj + ".chance");
        }
        return chance;
    }

    public boolean containsOnlyNumbers(String target) {
        boolean value = true;

        for (char c : target.toCharArray()) {
            if (!((c >= '0') && (c <= '9'))) {
                value = false;
                break;
            }
        }

        return value;
    }
}
