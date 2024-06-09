package me.sirimperivm.spigot.utils.colors;

import me.sirimperivm.spigot.Main;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("all")
public class Colors {

    private Main plugin;

    public Colors(Main plugin) {
        this.plugin = plugin;
    }

    public String translateString(String target) {
        return ChatColor.translateAlternateColorCodes('&', target);
    }
}
