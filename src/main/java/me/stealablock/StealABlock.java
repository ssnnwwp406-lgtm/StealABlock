package me.stealablock;

import me.stealablock.command.SABCommand;
import me.stealablock.selection.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StealABlock extends JavaPlugin {

    private SelectionManager selectionManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        selectionManager = new SelectionManager();

        SABCommand sabCommand = new SABCommand(this);

        if (getCommand("sab") != null) {
            getCommand("sab").setExecutor(sabCommand);
            getCommand("sab").setTabCompleter(sabCommand);
        }

        getServer().getPluginManager().registerEvents(
                sabCommand,
                this
        );

        getLogger().info("=================================");
        getLogger().info("        Steal A Block");
        getLogger().info("        Plugin Enabled");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {

        if (getConfig().getBoolean(
                "settings.save-on-disable",
                true
        )) {
            saveConfig();
        }

        getLogger().info("Steal A Block disabled.");
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
}
