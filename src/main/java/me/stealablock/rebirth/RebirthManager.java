package me.stealablock.rebirth;

import me.stealablock.StealABlock;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RebirthManager {

    private final StealABlock plugin;

    private final File file;

    private final YamlConfiguration data;

    public RebirthManager(
            StealABlock plugin
    ) {

        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(
                plugin.getDataFolder(),
                "rebirths.yml"
        );

        data =
                YamlConfiguration
                        .loadConfiguration(file);
    }

    // =========================
    // GET REBIRTH
    // =========================

    public int getRebirth(Player player) {

        return getRebirth(
                player.getUniqueId()
        );
    }

    public int getRebirth(UUID uuid) {

        return data.getInt(
                "players."
                        + uuid
                        + ".rebirth",
                0
        );
    }

    // =========================
    // SET REBIRTH
    // =========================

    public void setRebirth(
            Player player,
            int amount
    ) {

        setRebirth(
                player.getUniqueId(),
                amount
        );
    }

    public void setRebirth(
            UUID uuid,
            int amount
    ) {

        if (amount < 0) {
            amount = 0;
        }

        data.set(
                "players."
                        + uuid
                        + ".rebirth",
                amount
        );

        save();
    }

    // =========================
    // ADD REBIRTH
    // =========================

    public void addRebirth(
            Player player
    ) {

        int current =
                getRebirth(player);

        setRebirth(
                player,
                current + 1
        );
    }

    // =========================
    // SAVE
    // =========================

    public void save() {

        try {

            data.save(file);

        } catch (IOException exception) {

            plugin.getLogger().severe(
                    "Could not save rebirths.yml!"
            );

            exception.printStackTrace();
        }
    }
}
