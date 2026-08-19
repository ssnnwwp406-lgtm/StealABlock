package me.stealablock.rebirth;

import me.stealablock.StealABlock;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RebirthManager {

    private static final int MAX_REBIRTH = 40;

    private final StealABlock plugin;

    public RebirthManager(StealABlock plugin) {
        this.plugin = plugin;
    }

    public int getRebirth(Player player) {
        return getRebirth(
                player.getUniqueId()
        );
    }

    public int getRebirth(UUID uuid) {
        return plugin.getPlayerDataManager()
                .getRebirth(uuid);
    }

    public boolean canRebirth(Player player) {

        return getRebirth(player)
                < MAX_REBIRTH;
    }

    public boolean addRebirth(Player player) {

        int current =
                getRebirth(player);

        if (current >= MAX_REBIRTH) {
            return false;
        }

        plugin.getPlayerDataManager()
                .setRebirth(
                        player.getUniqueId(),
                        current + 1
                );

        return true;
    }

    public void setRebirth(
            Player player,
            int amount
    ) {

        plugin.getPlayerDataManager()
                .setRebirth(
                        player.getUniqueId(),
                        amount
                );
    }

    public int getMaxRebirth() {
        return MAX_REBIRTH;
    }

    public int getBaseLockTime(Player player) {

        int defaultTime =
                plugin.getConfig()
                        .getInt(
                                "rebirth.base-lock.default-time",
                                60
                        );

        int bonus =
                plugin.getConfig()
                        .getInt(
                                "rebirth.base-lock.bonus-per-rebirth",
                                20
                        );

        return defaultTime
                + (getRebirth(player) * bonus);
    }

    public double getIncomeMultiplier(
            Player player
    ) {

        int rebirth =
                getRebirth(player);

        return 1.0
                + (rebirth * 0.5);
    }
}
