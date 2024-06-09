package me.sirimperivm.spigot.utils.other;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.colors.Colors;

@SuppressWarnings("all")
public class Logger {

    private Main plugin;
    private Colors colors;

    private String pluginName;

    public Logger(Main plugin, String pluginName) {
        this.plugin = plugin;
        this.pluginName = pluginName;

        colors = plugin.getColors();
    }

    public void success(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&2[" + pluginName + "] " + message));
    }

    public void info(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&e[" + pluginName + "] " + message));
    }

    public void fail(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&c[" + pluginName + "] " + message));
    }
}
