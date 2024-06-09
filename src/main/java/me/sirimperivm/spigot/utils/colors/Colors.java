package me.sirimperivm.spigot.utils.colors;


import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.colors.rgb.RGBColor;

@SuppressWarnings("all")
public class Colors {

    private Main plugin;
    private RGBColor rgb;

    private int version;

    public Colors(Main plugin) {
        this.plugin = plugin;
        version = plugin.getServerVersion();
        rgb = new RGBColor(this);
    }
    public String translateString(String t) {
        return rgb.process(t);
    }

    public int getServerVersion() {
        return version;
    }
}
