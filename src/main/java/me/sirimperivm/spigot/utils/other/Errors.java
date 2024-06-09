package me.sirimperivm.spigot.utils.other;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class Errors {

    private Main plugin;
    private ConfigManager configManager;

    public Errors(Main plugin) {
        this.plugin = plugin;
        configManager = plugin.getConfigManager();
    }

    public boolean noPermCommand(CommandSender s, String node) {
        if (s.hasPermission(node))
            return false;
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "no-perm.command")
                .replace("{node}", node)
        );
        return true;
    }

    public boolean noPermAction(Player p, String node) {
        if (p.hasPermission(node))
            return false;
        p.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "no-perm.action")
                .replace("{node}", node)
        );
        return true;
    }

    public boolean noConsole(CommandSender s) {
        if (s instanceof Player)
            return false;
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "no-console"));
        return true;
    }
}
