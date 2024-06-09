package me.sirimperivm.spigot.utils.enchants;

import me.sirimperivm.spigot.Main;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

@SuppressWarnings("all")
public class Enchants {

    private Main plugin;

    private int serverVersion;
    private HashMap<String, Enchantment> enchantsList;

    public Enchants(Main plugin) {
        this.plugin = plugin;

        serverVersion = plugin.getServerVersion();
        enchantsList = new HashMap<>();

        setup();
    }

    private void setup() {
        if (serverVersion >= 8) {
            enchantsList.put("POWER", Enchantment.POWER);
            enchantsList.put("FLAME", Enchantment.FLAME);
            enchantsList.put("INFINITY", Enchantment.INFINITY);
            enchantsList.put("PUNCH", Enchantment.PUNCH);
            enchantsList.put("SHARPNESS", Enchantment.SHARPNESS);
            enchantsList.put("BANE_OF_ARTHROPODS", Enchantment.BANE_OF_ARTHROPODS);
            enchantsList.put("SMITE", Enchantment.SMITE);
            enchantsList.put("DEPTH_STRIDER", Enchantment.DEPTH_STRIDER);
            enchantsList.put("EFFICIENCY", Enchantment.EFFICIENCY);
            enchantsList.put("FIRE_ASPECT", Enchantment.FIRE_ASPECT);
            enchantsList.put("KNOCKBACK", Enchantment.KNOCKBACK);
            enchantsList.put("FORTUNE", Enchantment.FORTUNE);
            enchantsList.put("LOOTING", Enchantment.LOOTING);
            enchantsList.put("LUCK_OF_THE_SEA", Enchantment.LUCK_OF_THE_SEA);
            enchantsList.put("LURE", Enchantment.LURE);
            enchantsList.put("RESPIRATION", Enchantment.RESPIRATION);
            enchantsList.put("PROTECTION", Enchantment.PROTECTION);
            enchantsList.put("BLAST_PROTECTION", Enchantment.BLAST_PROTECTION);
            enchantsList.put("FIRE_PROTECTION", Enchantment.FIRE_PROTECTION);
            enchantsList.put("PROJECTILE_PROTECTION", Enchantment.PROJECTILE_PROTECTION);
            enchantsList.put("SILK_TOUCH", Enchantment.SILK_TOUCH);
            enchantsList.put("THORNS", Enchantment.THORNS);
            enchantsList.put("AQUA_AFFINITY", Enchantment.AQUA_AFFINITY);
        }

        if (serverVersion >= 9) {
            enchantsList.put("FROST_WALKER", Enchantment.FROST_WALKER);
            enchantsList.put("MENDING", Enchantment.MENDING);
        }

        if (serverVersion >= 11) {
            enchantsList.put("BINDING_CURSE", Enchantment.BINDING_CURSE);
            enchantsList.put("VANISHING_CURSE", Enchantment.VANISHING_CURSE);
        }

        if (serverVersion >= 13) {
            enchantsList.put("CHANNELING", Enchantment.CHANNELING);
            enchantsList.put("IMPALING", Enchantment.IMPALING);
            enchantsList.put("LOYALTY", Enchantment.LOYALTY);
            enchantsList.put("RIPTIDE", Enchantment.RIPTIDE);
        }

        if (serverVersion >= 14) {
            enchantsList.put("MULTISHOT", Enchantment.MULTISHOT);
            enchantsList.put("PIERCING", Enchantment.PIERCING);
            enchantsList.put("QUICK_CHARGE", Enchantment.QUICK_CHARGE);
        }

        if (serverVersion >= 16) {
            enchantsList.put("SOUL_SPEED", Enchantment.SOUL_SPEED);
        }

        if (serverVersion >= 19) {
            enchantsList.put("SWIFT_SNEAK", Enchantment.SWIFT_SNEAK);
        }

        if (serverVersion >= 21) {
            enchantsList.put("DENSITY", Enchantment.DENSITY);
            enchantsList.put("WIND_BURST", Enchantment.WIND_BURST);
        }
    }

    public Enchantment getEnchant(String enchantName) {
        String original = enchantName.toUpperCase();
        if (enchantsList.containsKey(original)) {
            return enchantsList.get(original);
        }
        return null;
    }
}
