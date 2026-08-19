package me.stealablock.prestige;

import me.stealablock.StealABlock;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrestigeManager {

    private final StealABlock plugin;

    public PrestigeManager(
            StealABlock plugin
    ) {
        this.plugin = plugin;
    }

    public int getPrestige(Player player) {
        return getPrestige(
                player.getUniqueId()
        );
    }

    public int getPrestige(UUID uuid) {
        return plugin.getPlayerDataManager()
                .getPrestige(uuid);
    }

    public void setPrestige(
            Player player,
            int amount
    ) {

        plugin.getPlayerDataManager()
                .setPrestige(
                        player.getUniqueId(),
                        amount
                );
    }

    public void addPrestige(
            Player player
    ) {

        setPrestige(
                player,
                getPrestige(player) + 1
        );
    }

    public int getSlots(Player player) {

        int startingSlots =
                plugin.getConfig()
                        .getInt(
                                "base.starting-slots",
                                10
                        );

        int slotsPerPrestige =
                plugin.getConfig()
                        .getInt(
                                "base.slots-per-prestige",
                                10
                        );

        return startingSlots
                + (
                getPrestige(player)
                        * slotsPerPrestige
        );
    }

    public int getFloors(Player player) {

        int startingFloors =
                plugin.getConfig()
                        .getInt(
                                "base.starting-floors",
                                1
                        );

        int floorsPerPrestige =
                plugin.getConfig()
                        .getInt(
                                "base.floors-per-prestige",
                                1
                        );

        return startingFloors
                + (
                getPrestige(player)
                        * floorsPerPrestige
        );
    }
          }
