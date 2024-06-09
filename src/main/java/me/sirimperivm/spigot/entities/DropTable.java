package me.sirimperivm.spigot.entities;

import org.bukkit.inventory.ItemStack;

@SuppressWarnings("all")
public class DropTable {

    private ItemStack drop;
    private int amount;
    private double chance;

    public DropTable(ItemStack drop, int amount, double chance) {
        this.drop = drop;
        this.amount = amount;
        this.chance = chance;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public int getAmount() {
        return amount;
    }

    public double getChance() {
        return chance;
    }
}
