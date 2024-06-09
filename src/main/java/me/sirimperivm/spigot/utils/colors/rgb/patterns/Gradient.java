package me.sirimperivm.spigot.utils.colors.rgb.patterns;


import me.sirimperivm.spigot.utils.colors.rgb.RGBColor;

import java.awt.*;
import java.util.regex.Matcher;

@SuppressWarnings("all")
public class Gradient implements Pattern {
    private RGBColor rgb;
    public Gradient(RGBColor rgb) {
        this.rgb = rgb;
    }

    java.util.regex.Pattern patt = java.util.regex.Pattern.compile("->G:([0-9A-Fa-f]{6})/(.*?)/->G:([0-9A-Fa-f]{6})");

    @Override
    public String process(String string) {
        Matcher match = patt.matcher(string);
        while (match.find()) {
            String start = match.group(1);
            String end = match.group(3);
            String content = match.group(2);
            string = string.replace(match.group(), rgb.color(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))));
        }
        return string;
    }
}