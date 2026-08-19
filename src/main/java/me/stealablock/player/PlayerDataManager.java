package me.stealablock.player;

import me.stealablock.StealABlock;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private final StealABlock plugin;
    private final File file;
    private final YamlConfiguration data;

    public PlayerDataManager(StealABlock plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(
                plugin.getDataFolder(),
                "players.yml"
        );

        data = YamlConfiguration.loadConfiguration(file);
    }

    public boolean hasPlayer(UUID uuid) {
        return data.contains("players." + uuid);
    }

    public void createPlayer(UUID uuid) {

        String path = "players." + uuid;

        if (!data.contains(path)) {

            data.set(path + ".money", 0.0);
            data.set(path + ".rebirth", 0);
            data.set(path + ".prestige", 0);
            data.set(path + ".language", "en");

            save();
        }
    }

    public double getMoney(UUID uuid) {
        createPlayer(uuid);

        return data.getDouble(
                "players." + uuid + ".money",
                0.0
        );
    }

    public void setMoney(UUID uuid, double amount) {

        createPlayer(uuid);

        data.set(
                "players." + uuid + ".money",
                Math.max(0, amount)
        );

        save();
    }

    public void addMoney(UUID uuid, double amount) {

        setMoney(
                uuid,
                getMoney(uuid) + amount
        );
    }

    public int getRebirth(UUID uuid) {
        createPlayer(uuid);

        return data.getInt(
                "players." + uuid + ".rebirth",
                0
        );
    }

    public void setRebirth(
            UUID uuid,
            int amount
    ) {

        createPlayer(uuid);

        amount = Math.max(
                0,
                Math.min(amount, 40)
        );

        data.set(
                "players." + uuid + ".rebirth",
                amount
        );

        save();
    }

    public int getPrestige(UUID uuid) {
        createPlayer(uuid);

        return data.getInt(
                "players." + uuid + ".prestige",
                0
        );
    }

    public void setPrestige(
            UUID uuid,
            int amount
    ) {

        createPlayer(uuid);

        data.set(
                "players." + uuid + ".prestige",
                Math.max(0, amount)
        );

        save();
    }

    public String getLanguage(UUID uuid) {
        createPlayer(uuid);

        return data.getString(
                "players." + uuid + ".language",
                "en"
        );
    }

    public void setLanguage(
            UUID uuid,
            String language
    ) {

        createPlayer(uuid);

        data.set(
                "players." + uuid + ".language",
                language
        );

        save();
    }

    public void save() {

        try {

            data.save(file);

        } catch (IOException exception) {

            plugin.getLogger().severe(
                    "Could not save players.yml!"
            );

            exception.printStackTrace();
        }
    }
          }
