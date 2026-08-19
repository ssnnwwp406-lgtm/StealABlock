package me.stealablock;

import me.stealablock.base.BaseLockManager;
import me.stealablock.base.BaseManager;
import me.stealablock.block.BlockManager;
import me.stealablock.block.BlockSpawner;
import me.stealablock.command.SABCommand;
import me.stealablock.rebirth.RebirthManager;
import me.stealablock.selection.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StealABlock extends JavaPlugin {

    private SelectionManager selectionManager;
    private BaseManager baseManager;
    private BaseLockManager baseLockManager;

    private BlockManager blockManager;
    private BlockSpawner blockSpawner;

    private RebirthManager rebirthManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Selection
        selectionManager =
                new SelectionManager();

        // Bases
        baseManager =
                new BaseManager(this);

        // Rebirth
        rebirthManager =
                new RebirthManager(this);

        // Base Lock
        baseLockManager =
                new BaseLockManager(this);

        // Blocks
        blockManager =
                new BlockManager();

        // Block Spawner
        blockSpawner =
                new BlockSpawner(this);

        blockSpawner.start();

        // Command
        SABCommand sabCommand =
                new SABCommand(this);

        if (getCommand("sab") != null) {

            getCommand("sab")
                    .setExecutor(sabCommand);

            getCommand("sab")
                    .setTabCompleter(sabCommand);
        }

        // Events
        getServer()
                .getPluginManager()
                .registerEvents(
                        sabCommand,
                        this
                );

        getLogger().info(
                "StealABlock enabled!"
        );
    }

    @Override
    public void onDisable() {

        if (baseLockManager != null) {
            baseLockManager.shutdown();
        }

        if (baseManager != null) {
            baseManager.save();
        }

        if (rebirthManager != null) {
            rebirthManager.save();
        }

        saveConfig();

        getLogger().info(
                "StealABlock disabled!"
        );
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public BaseManager getBaseManager() {
        return baseManager;
    }

    public BaseLockManager getBaseLockManager() {
        return baseLockManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public BlockSpawner getBlockSpawner() {
        return blockSpawner;
    }

    public RebirthManager getRebirthManager() {
        return rebirthManager;
    }
}
