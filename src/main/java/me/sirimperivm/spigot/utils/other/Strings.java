package me.sirimperivm.spigot.utils.other;

import me.sirimperivm.spigot.Main;

@SuppressWarnings("all")
public class Strings {

    private Main plugin;
    public Strings(Main plugin) {
        this.plugin = plugin;
    }

    public String capitalize(String target) {
        return target.substring(0, 1).toUpperCase()+target.substring(1).toLowerCase();
    }
}
