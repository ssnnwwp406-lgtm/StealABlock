package me.stealablock;

import me.stealablock.base.BaseLockManager;
import me.stealablock.base.BaseManager;
import me.stealablock.block.BlockManager;
import me.stealablock.block.BlockSpawner;
import me.stealablock.command.SABCommand;
import me.stealablock.economy.MoneyManager;
import me.stealablock.listener.PlayerJoinListener;
import me.stealablock.player.PlayerDataManager;
import me.stealablock.prestige.PrestigeManager;
import me.stealablock.rebirth.RebirthManager;
import me.stealablock.scoreboard.SABScoreboard;
import me.stealablock.selection.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StealABlock extends JavaPlugin {

    private SelectionManager selectionManager;
    private BaseManager baseManager;
    private BaseLockManager baseLockManager;

    private BlockManager blockManager;
    private BlockSpawner blockSpawner;

    private PlayerDataManager playerDataManager;
    private MoneyManager moneyManager;
    private RebirthManager rebirthManager;
    private PrestigeManager prestigeManager;

    private SABScoreboard scoreboard;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        selectionManager =
                new SelectionManager();

        baseManager =
                new BaseManager(this);

        playerDataManager =
                new PlayerDataManager(this);

        moneyManager =
                new MoneyManager(this);

        rebirthManager =
                new RebirthManager(this);

        prestigeManager =
                new PrestigeManager(this);

        baseLockManager =
                new BaseLockManager(this);

        blockManager =
                new BlockManager();

        scoreboard =
                new SABScoreboard(this);

        blockSpawner =
                new BlockSpawner(this);

        blockSpawner.start();

        SABCommand sabCommand =
                new SABCommand(this);

        if (getCommand("sab") != null) {

            getCommand("sab")
                    .setExecutor(sabCommand);

            getCommand("sab")
                    .setTabCompleter(sabCommand);
        }

        getServer()
                .getPluginManager()
                .registerEvents(
                        sabCommand,
                        this
                );

        getServer()
                .getPluginManager()
                .registerEvents(
                        new PlayerJoinListener(this),
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

        if (playerDataManager != null) {
            playerDataManager.save();
        }

        if (baseManager != null) {
            baseManager.save();
        }
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

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public MoneyManager getMoneyManager() {
        return moneyManager;
    }

    public RebirthManager getRebirthManager() {
        return rebirthManager;
    }

    public PrestigeManager getPrestigeManager() {
        return prestigeManager;
    }

    public SABScoreboard getScoreboard() {
        return scoreboard;
    }
                               }
