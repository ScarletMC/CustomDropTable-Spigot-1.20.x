package me.sirimperivm.spigot.utils;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.entities.DropTable;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.enchants.Enchants;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        List<DropTable> dropTables = new ArrayList<>();
        for (String key : configManager.getTables().getConfigurationSection(type + "." + target + ".custom-drops").getKeys(false)) {
            String path = type + "." + target + ".custom-drops." + key;

            int minAmount = configManager.getTables().getInt(path + ".min-amount");
            int maxAmount = configManager.getTables().getInt(path + ".max-amount");
            int amount = getRandomAmount(minAmount, maxAmount);

            String materialName = configManager.getTables().getString(path + ".type");
            if (materialName.equals("AIR")) continue;
            ItemStack itemStack = new ItemStack(Material.getMaterial(materialName), amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (materialName.endsWith("POTION") && itemMeta instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) itemMeta;
                for (String potionEffectString : configManager.getTables().getStringList(path + ".potion-effects")) {
                    String[] splitter = potionEffectString.split(":");
                    String effectName = splitter[0];
                    int effectLevel;
                    int effectDuration;
                    try {
                        effectLevel = Integer.parseInt(splitter[1]);
                        effectDuration = Integer.parseInt(splitter[2]);
                    } catch (NumberFormatException e) {
                        log.fail("Impossibile aggiungere l'effetto della pozione " + potionEffectString + ", il livello o la durata non sono numeri validi.");
                        continue;
                    }

                    effectLevel--;
                    if (effectLevel < 0) {
                        log.fail("Impossibile aggiungere l'effetto della pozione " + potionEffectString + ", il livello è negativo.");
                        continue;
                    }

                    if (effectDuration < 1) {
                        log.fail("Impossibile aggiungere l'effetto della pozione " + potionEffectString + ", la durata è negativa.");
                        continue;
                    }

                    PotionEffectType effectType = Registry.EFFECT.get(NamespacedKey.minecraft(effectName.toLowerCase()));
                    if (effectType != null) {
                        PotionEffect potionEffect = new PotionEffect(effectType, 20*effectDuration, effectLevel);
                        potionMeta.addCustomEffect(potionEffect, true);
                    } else {
                        log.fail("L'effetto " + effectName + " non esiste.");
                        continue;
                    }
                }

                itemMeta = potionMeta;
            }
            String display = configManager.getTables().getString(path + ".display");
            if (!display.equals("null")) {
                itemMeta.setDisplayName(colors.translateString(display));
            }
            itemMeta.setLore(configManager.getTranslatedList(configManager.getTables(), path + ".lore"));
            for (String enchantString : configManager.getTables().getStringList(path + ".enchants")) {
                String[] splitter = enchantString.split(":");
                String enchantName = splitter[0];
                int enchantLevel = 0;
                try {
                    enchantLevel = Integer.parseInt(splitter[1]);
                } catch (NumberFormatException e) {
                    log.fail("Impossibile aggiungere un incantesimo ad un drop, il livello inserito non è un numero.");
                    continue;
                }

                if (enchantLevel <= 0) {
                    log.fail("Impossibile aggiungere un incantesimo ad un drop, il livello inserito non è valido.");
                    continue;
                }

                Enchantment enchant = enchants.getEnchant(enchantName);
                if (enchant != null) {
                    itemMeta.addEnchant(enchants.getEnchant(enchantName), enchantLevel, true);
                } else {
                    log.fail("L'incantesimo " + enchantName + " non esiste.");
                    continue;
                }
            }
            for (String flag : configManager.getTables().getStringList(path + ".flags")) {
                itemMeta.addItemFlags(ItemFlag.valueOf(flag));
            }
            boolean unbreakable = configManager.getTables().getBoolean(path + ".unbreakable");
            itemMeta.setUnbreakable(unbreakable);
            int modelData = configManager.getTables().getInt(path + ".model");
            if (modelData > 0) {
                itemMeta.setCustomModelData(modelData);
            } else if (modelData < 0) {
                log.fail("Impossibile dare un modello negativo ad un drop.");
            }
            for (String attributeString : configManager.getTables().getStringList(path + ".attributes")) {
                String[] splitter = attributeString.split(":");
                String attributeName = splitter[0];
                Attribute attribute = Attribute.valueOf(attributeName);
                double attributeModifierValue = 0.0;
                try {
                    attributeModifierValue = Double.parseDouble(splitter[1]);
                } catch (NumberFormatException e) {
                    log.fail("Impossibile aggiungere un attributo ad un drop, il suo modificatore non è un numero decimale.");
                    continue;
                }

                if (attributeModifierValue <= 0.0) {
                    log.fail("Impossibile aggiungere un attributo ad un drop, il suo modificatore non è valido.");
                    continue;
                }

                AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attribute.name(), attributeModifierValue, AttributeModifier.Operation.ADD_NUMBER);
                itemMeta.addAttributeModifier(attribute, attributeModifier);
            }
            itemStack.setItemMeta(itemMeta);

            double chance = configManager.getTables().getDouble(path + ".chance");
            DropTable dropTable = new DropTable(itemStack, amount, chance);
            dropTables.add(dropTable);
        }

        double randomValue = Math.random() * 100;
        double cumulativeChance = 0;

        for (DropTable dropTable : dropTables) {
            cumulativeChance += dropTable.getChance();
            if (randomValue <= cumulativeChance) {
                return dropTable.getDrop();
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
        for (String duplicateObj : configManager.getTables().getConfigurationSection(type).getKeys(false)) {
            if (duplicateObj.equals(target))
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
