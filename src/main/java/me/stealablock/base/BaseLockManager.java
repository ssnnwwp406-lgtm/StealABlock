package me.stealablock.base;

import me.stealablock.StealABlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BaseLockManager {

    private final StealABlock plugin;

    private final Map<String, BukkitRunnable> activeTimers =
            new HashMap<>();

    private final Map<String, Long> unlockTimes =
            new HashMap<>();

    public BaseLockManager(StealABlock plugin) {
        this.plugin = plugin;
    }

    // =========================
    // OPEN BASE
    // =========================

    public boolean openBase(
            Player player,
            Base base
    ) {

        if (base == null) {
            return false;
        }

        if (base.getOwner() == null) {
            return false;
        }

        if (!base.getOwner().equals(player.getUniqueId())) {
            return false;
        }

        if (!base.isLocked()) {
            return false;
        }

        int seconds =
                getLockTime(player);

        base.setLocked(false);

        long unlockTime =
                System.currentTimeMillis()
                        + (seconds * 1000L);

        unlockTimes.put(
                base.getName().toLowerCase(),
                unlockTime
        );

        startTimer(
                base,
                seconds
        );

        return true;
    }

    // =========================
    // CLOSE BASE
    // =========================

    public void closeBase(Base base) {

        if (base == null) {
            return;
        }

        String key =
                base.getName().toLowerCase();

        BukkitRunnable task =
                activeTimers.remove(key);

        if (task != null) {
            task.cancel();
        }

        unlockTimes.remove(key);

        base.setLocked(true);
    }

    // =========================
    // TIMER
    // =========================

    private void startTimer(
            Base base,
            int seconds
    ) {

        String key =
                base.getName().toLowerCase();

        BukkitRunnable oldTask =
                activeTimers.remove(key);

        if (oldTask != null) {
            oldTask.cancel();
        }

        BukkitRunnable task =
                new BukkitRunnable() {

                    int remaining = seconds;

                    @Override
                    public void run() {

                        if (base.isLocked()) {
                            cancel();
                            activeTimers.remove(key);
                            unlockTimes.remove(key);
                            return;
                        }

                        if (remaining <= 0) {

                            closeBase(base);

                            notifyOwner(
                                    base,
                                    "§c🔒 Your base has been locked!"
                            );

                            cancel();

                            return;
                        }

                        remaining--;
                    }
                };

        activeTimers.put(
                key,
                task
        );

        task.runTaskTimer(
                plugin,
                20L,
                20L
        );
    }

    // =========================
    // GET REMAINING TIME
    // =========================

    public long getRemainingSeconds(
            Base base
    ) {

        if (base == null || base.isLocked()) {
            return 0;
        }

        Long unlockTime =
                unlockTimes.get(
                        base.getName().toLowerCase()
                );

        if (unlockTime == null) {
            return 0;
        }

        long remaining =
                unlockTime
                        - System.currentTimeMillis();

        return Math.max(
                0,
                (remaining + 999) / 1000
        );
    }

    // =========================
    // GET LOCK TIME
    // =========================

    public int getLockTime(Player player) {

        int defaultTime =
                plugin.getConfig()
                        .getInt(
                                "base.lock.default-time",
                                60
                        );

        int rebirthBonus =
                plugin.getConfig()
                        .getInt(
                                "base.lock.rebirth-bonus",
                                20
                        );

        int rebirth =
                plugin.getRebirthManager()
                        .getRebirth(
                                player
                        );

        return defaultTime
                + (rebirth * rebirthBonus);
    }

    // =========================
    // CHECK LOCK
    // =========================

    public boolean isLocked(Base base) {

        return base == null
                || base.isLocked();
    }

    public boolean isOpen(Base base) {

        return base != null
                && !base.isLocked();
    }

    // =========================
    // OWNER MESSAGE
    // =========================

    private void notifyOwner(
            Base base,
            String message
    ) {

        if (base.getOwner() == null) {
            return;
        }

        Player owner =
                plugin.getServer()
                        .getPlayer(
                                base.getOwner()
                        );

        if (owner != null) {
            owner.sendMessage(message);
        }
    }

    // =========================
    // SHUTDOWN
    // =========================

    public void shutdown() {

        for (BukkitRunnable task :
                activeTimers.values()) {

            task.cancel();
        }

        activeTimers.clear();
        unlockTimes.clear();
    }
          }
