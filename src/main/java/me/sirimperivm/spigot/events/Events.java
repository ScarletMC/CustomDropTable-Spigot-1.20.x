package me.sirimperivm.spigot.events;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.enchants.Enchants;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("all")
public class Events implements Listener {

    private Main plugin;
    private Strings strings;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private Enchants enchants;
    private ModuleManager moduleManager;

    public Events(Main plugin) {
        this.plugin = plugin;
        strings = plugin.getStrings();
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        enchants = plugin.getEnchants();
        moduleManager = plugin.getModuleManager();
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        Entity en = e.getEntity();

        if (en instanceof Player)
            return;

        String mobName = en.getType().toString();
        String mobNameUpper = mobName.toUpperCase();

        if (configManager.getTables().getConfigurationSection("mobs." + mobNameUpper) == null)
            return;

        int duplicates = moduleManager.searchDuplicates("mobs", mobNameUpper);
        if (duplicates > 1) {
            Bukkit.broadcastMessage(configManager.getTranslatedString(configManager.getMessages(), "drop-tables-errors.mobs.duplicates-found")
                    .replace("{duplicates}", String.valueOf(duplicates))
                    .replace("{mob-drop-table}", strings.capitalize(mobName))
                    .replace("{mob-drop-table-upper}", mobNameUpper)
            );
            return;
        }
        if (duplicates < 1)
            return;

        double maxChance = moduleManager.searchMaxChance("mobs", mobNameUpper);
        if (maxChance != 100.0) {
            Bukkit.broadcastMessage(configManager.getTranslatedString(configManager.getMessages(), "drop-tables-errors.mobs.max-chance-invalid")
                    .replace("{mob-drop-table}", strings.capitalize(mobName))
                    .replace("{mob-drop-table-upper}", mobNameUpper)
            );
            return;
        }

        ItemStack randomDrop = moduleManager.getRandomDrop("mobs", mobNameUpper);
        if (randomDrop == null)
            return;

        boolean replaceDrops = configManager.getTables().getBoolean("mobs." + mobNameUpper + ".replace-default-drops");
        if (replaceDrops) {
            e.getDrops().clear();
            e.getDrops().add(randomDrop);
            return;
        }
        e.getDrops().add(randomDrop);
        return;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block b = e.getBlock();

        if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL)
            return;

        String blockName = b.getType().toString();
        String blockNameUpper = blockName.toUpperCase();

        if (configManager.getTables().getConfigurationSection("blocks." + blockNameUpper) == null)
            return;

        int duplicates = moduleManager.searchDuplicates("blocks", blockNameUpper);
        if (duplicates > 1) {
            Bukkit.broadcastMessage(configManager.getTranslatedString(configManager.getMessages(), "drop-tables-errors.blocks.duplicates-found")
                    .replace("{duplicates}", String.valueOf(duplicates))
                    .replace("{block-drop-table}", strings.capitalize(blockName))
                    .replace("{block-drop-table-upper}", blockNameUpper)
            );
            return;
        }
        if (duplicates < 1)
            return;

        double maxChance = moduleManager.searchMaxChance("blocks", blockNameUpper);
        if (maxChance != 100.0) {
            Bukkit.broadcastMessage(configManager.getTranslatedString(configManager.getMessages(), "drop-tables-errors.blocks.max-chance-invalid")
                    .replace("{block-drop-table}", strings.capitalize(blockName))
                    .replace("{block-drop-table-upper}", blockNameUpper)
            );
            return;
        }

        ItemStack randomDrop = moduleManager.getRandomDrop("blocks", blockNameUpper);
        if (randomDrop == null)
            return;

        boolean replaceDrops = configManager.getTables().getBoolean("blocks." + blockNameUpper + ".replace-default-drops");
        if (replaceDrops) {
            e.setDropItems(false);
            b.getWorld().dropItemNaturally(b.getLocation(), randomDrop);
            return;
        }
        b.getWorld().dropItemNaturally(b.getLocation(), randomDrop);
        return;
    }
}
