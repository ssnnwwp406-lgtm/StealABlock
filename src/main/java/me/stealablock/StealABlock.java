package me.stealablock;

import me.stealablock.base.BaseManager;
import me.stealablock.command.SABCommand;
import me.stealablock.selection.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StealABlock extends JavaPlugin {

    private SelectionManager selectionManager;
    private BaseManager baseManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        selectionManager =
                new SelectionManager();

        baseManager =
                new BaseManager(this);

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

        getLogger().info(
                "Steal A Block enabled!"
        );
    }

    @Override
    public void onDisable() {

        if (baseManager != null) {
            baseManager.save();
        }

        saveConfig();

        getLogger().info(
                "Steal A Block disabled!"
        );
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public BaseManager getBaseManager() {
        return baseManager;
    }
}
