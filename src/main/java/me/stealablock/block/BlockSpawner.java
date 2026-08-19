package me.stealablock.block;

import me.stealablock.StealABlock;
import me.stealablock.base.Base;
import me.stealablock.rarity.Rarity;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class BlockSpawner {

    private final StealABlock plugin;

    private final Random random =
            new Random();

    public BlockSpawner(
            StealABlock plugin
    ) {
        this.plugin = plugin;
    }

    public void start() {

        for (Rarity rarity : Rarity.values()) {

            startSpawner(rarity);
        }

        plugin.getLogger().info(
                "Block spawners started!"
        );
    }

    private void startSpawner(
            Rarity rarity
    ) {

        long ticks =
                rarity.getInterval() / 50L;

        new BukkitRunnable() {

            @Override
            public void run() {

                spawnBlocks(rarity);
            }

        }.runTaskTimer(
                plugin,
                ticks,
                ticks
        );
    }

    private void spawnBlocks(
            Rarity rarity
    ) {

        List<BlockData> available =
                plugin.getBlockManager()
                        .getBlocks(rarity);

        if (available.isEmpty()) {
            return;
        }

        BlockData block =
                available.get(
                        random.nextInt(
                                available.size()
                        )
                );

        for (Base base :
                plugin.getBaseManager()
                        .getBases()) {

            if (!base.hasSpawn()) {
                continue;
            }

            spawnBlock(
                    base.getSpawn(),
                    block
            );
        }
    }

    private void spawnBlock(
            Location location,
            BlockData block
    ) {

        Location spawnLocation =
                location.clone();

        ItemDisplay display =
                spawnLocation.getWorld()
                        .spawn(
                                spawnLocation,
                                ItemDisplay.class
                        );

        display.setItemStack(
                new ItemStack(
                        block.getMaterial()
                )
        );

        display.setCustomName(
                "§f"
                        + formatName(
                        block.getMaterial()
                )
                        + " §8• §f"
                        + block.getRarity()
                                .getDisplayName()
        );

        display.setCustomNameVisible(
                true
        );

        display.setPersistent(
                true
        );
    }

    private String formatName(
            org.bukkit.Material material
    ) {

        String[] parts =
                material.name()
                        .toLowerCase()
                        .split("_");

        StringBuilder name =
                new StringBuilder();

        for (String part : parts) {

            if (name.length() > 0) {
                name.append(" ");
            }

            name.append(
                    Character.toUpperCase(
                            part.charAt(0)
                    )
            );

            name.append(
                    part.substring(1)
            );
        }

        return name.toString();
    }
          }
