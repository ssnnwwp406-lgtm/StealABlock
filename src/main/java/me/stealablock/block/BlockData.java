package me.stealablock.block;

import me.stealablock.rarity.Rarity;
import org.bukkit.Material;

public class BlockData {

    private final Material material;
    private final Rarity rarity;

    private final double price;
    private final double income;

    public BlockData(
            Material material,
            Rarity rarity,
            double price,
            double income
    ) {
        this.material = material;
        this.rarity = rarity;
        this.price = price;
        this.income = income;
    }

    public Material getMaterial() {
        return material;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public double getPrice() {
        return price;
    }

    public double getIncome() {
        return income;
    }
}
