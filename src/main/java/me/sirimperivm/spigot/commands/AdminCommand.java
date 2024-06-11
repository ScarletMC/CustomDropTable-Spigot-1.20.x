package me.sirimperivm.spigot.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class AdminCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager moduleManager;

    public AdminCommand(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        moduleManager = plugin.getModuleManager();
    }

    private void getUsage(CommandSender s, int page) {
        moduleManager.createHelp(s, "admin-command", page);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.admin.main"))) {
            return true;
        } else {
            if (a.length == 0) {
                getUsage(s, 1);
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("reload")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.admin.reload"))) {
                        return true;
                    } else {
                        configManager.loadAll();
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "plugin-reloaded"));
                    }
                } else {
                    getUsage(s, 1);
                }
            } else if (a.length == 2) {
                if (a[0].equalsIgnoreCase("help")) {
                    if (moduleManager.containsOnlyNumbers(a[1])) {
                        int page = Integer.parseInt(a[1]);
                        getUsage(s, page);
                    } else {
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.only-numbers-allowed").replace("{arg}", a[1]));
                    }
                } else {
                    getUsage(s, 1);
                }
            } else {
                getUsage(s, 1);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 1) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.admin.main"))) {
                toReturn.add("help");

                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.admin.reload"))) {
                    toReturn.add("reload");
                }
            }
            return toReturn;
        } else if (a.length == 2) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.admin.main"))) {
                if (a[0].equalsIgnoreCase("help")) {
                    toReturn.add("[page]");
                }
            }
            return toReturn;
        }
        return new ArrayList<>();
    }
}
