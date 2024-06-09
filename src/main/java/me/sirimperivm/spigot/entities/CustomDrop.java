package me.sirimperivm.spigot.entities;

import org.bukkit.Material;

@SuppressWarnings("all")
public class CustomDrop {

    private String id;
    private Material type;
    private int minAmount;
    private int maxAmount;
    private double chance;

    public CustomDrop(String id, Material type, int minAmount, int maxAmount, double chance) {
        this.id = id;
        this.type = type;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public Material getType() {
        return type;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public double getChance() {
        return chance;
    }
}
