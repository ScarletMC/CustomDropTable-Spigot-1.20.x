package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;

    @Override
    public void onEnable() {
        plugin = this;
        colors = new Colors(plugin);
        log = new Logger(plugin, "CustomDropTable");
        configManager = new ConfigManager(plugin);
        errors = new Errors(plugin);

        log.success("Plugin attivato correttamente.");
    }

    @Override
    public void onDisable() {
        log.success("Plugin disattivato correttamente.");
    }

    public Main getPlugin() {
        return plugin;
    }

    public Colors getColors() {
        return colors;
    }

    public Logger getLog() {
        return log;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Errors getErrors() {
        return errors;
    }
}
