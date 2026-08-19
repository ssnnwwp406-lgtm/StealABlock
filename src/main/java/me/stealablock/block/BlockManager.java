package me.stealablock.block;

import me.stealablock.rarity.Rarity;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {

    private final List<BlockData> blocks =
            new ArrayList<>();

    public BlockManager() {
        registerDefaultBlocks();
    }

    private void registerDefaultBlocks() {

        // =========================
        // COMMON
        // =========================

        register(
                Material.DIRT,
                Rarity.COMMON,
                100,
                2
        );

        register(
                Material.STONE,
                Rarity.COMMON,
                200,
                4
        );

        register(
                Material.OAK_LOG,
                Rarity.COMMON,
                300,
                6
        );

        // =========================
        // UNCOMMON
        // =========================

        register(
                Material.COAL_ORE,
                Rarity.UNCOMMON,
                1_000,
                20
        );

        register(
                Material.IRON_ORE,
                Rarity.UNCOMMON,
                2_500,
                50
        );

        // =========================
        // RARE
        // =========================

        register(
                Material.GOLD_ORE,
                Rarity.RARE,
                10_000,
                200
        );

        // =========================
        // EPIC
        // =========================

        register(
                Material.DIAMOND_ORE,
                Rarity.EPIC,
                75_000,
                1_500
        );

        // =========================
        // LEGENDARY
        // =========================

        register(
                Material.DIAMOND_BLOCK,
                Rarity.LEGENDARY,
                1_000_000,
                25_000
        );

        // =========================
        // MYTHIC
        // =========================

        register(
                Material.ANCIENT_DEBRIS,
                Rarity.MYTHIC,
                10_000_000,
                300_000
        );

        // =========================
        // DIVINE
        // =========================

        register(
                Material.NETHERITE_BLOCK,
                Rarity.DIVINE,
                100_000_000,
                3_000_000
        );

        // =========================
        // SECRET
        // =========================

        register(
                Material.BEACON,
                Rarity.SECRET,
                500_000_000,
                10_000_000
        );
    }

    public void register(
            Material material,
            Rarity rarity,
            double price,
            double income
    ) {

        blocks.add(
                new BlockData(
                        material,
                        rarity,
                        price,
                        income
                )
        );
    }

    public List<BlockData> getBlocks() {
        return blocks;
    }

    public List<BlockData> getBlocks(
            Rarity rarity
    ) {

        return blocks.stream()
                .filter(block ->
                        block.getRarity() == rarity
                )
                .toList();
    }
}
