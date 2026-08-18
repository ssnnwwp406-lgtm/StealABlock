package me.stealablock.base;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseManager {

    private final JavaPlugin plugin;

    private final Map<String, Base> bases = new HashMap<>();

    private final File file;

    private YamlConfiguration config;

    public BaseManager(JavaPlugin plugin) {

        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(
                plugin.getDataFolder(),
                "bases.yml"
        );

        load();
    }

    // =================================================
    // CREATE / SET
    // =================================================

    public boolean createBase(
            String name,
            Location pos1,
            Location pos2
    ) {

        if (bases.containsKey(name.toLowerCase())) {
            return false;
        }

        Base base = new Base(name);

        base.setPos1(pos1);
        base.setPos2(pos2);

        bases.put(
                name.toLowerCase(),
                base
        );

        save();

        return true;
    }

    // =================================================
    // GET
    // =================================================

    public Base getBase(String name) {

        if (name == null) {
            return null;
        }

        return bases.get(
                name.toLowerCase()
        );
    }

    // =================================================
    // DELETE
    // =================================================

    public boolean deleteBase(String name) {

        if (!bases.containsKey(name.toLowerCase())) {
            return false;
        }

        bases.remove(
                name.toLowerCase()
        );

        save();

        return true;
    }

    // =================================================
    // ALL BASES
    // =================================================

    public Collection<Base> getBases() {
        return bases.values();
    }

    // =================================================
    // SET SPAWN
    // =================================================

    public boolean setSpawn(
            String name,
            Location location
    ) {

        Base base = getBase(name);

        if (base == null) {
            return false;
        }

        base.setSpawn(location);

        save();

        return true;
    }

    // =================================================
    // SAVE
    // =================================================

    public void save() {

        config = new YamlConfiguration();

        for (Base base : bases.values()) {

            String path =
                    "bases." + base.getName();

            config.set(
                    path + ".pos1",
                    serializeLocation(
                            base.getPos1()
                    )
            );

            config.set(
                    path + ".pos2",
                    serializeLocation(
                            base.getPos2()
                    )
            );

            if (base.hasSpawn()) {

                config.set(
                        path + ".spawn",
                        serializeLocation(
                                base.getSpawn()
                        )
                );
            }
        }

        try {

            config.save(file);

        } catch (IOException exception) {

            plugin.getLogger().severe(
                    "Could not save bases.yml!"
            );

            exception.printStackTrace();
        }
    }

    // =================================================
    // LOAD
    // =================================================

    public void load() {

        if (!file.exists()) {
            return;
        }

        config =
                YamlConfiguration.loadConfiguration(
                        file
                );

        ConfigurationSection section =
                config.getConfigurationSection(
                        "bases"
                );

        if (section == null) {
            return;
        }

        for (String name : section.getKeys(false)) {

            Base base = new Base(name);

            Location pos1 =
                    deserializeLocation(
                            config.getConfigurationSection(
                                    "bases." + name + ".pos1"
                            )
                    );

            Location pos2 =
                    deserializeLocation(
                            config.getConfigurationSection(
                                    "bases." + name + ".pos2"
                            )
                    );

            Location spawn =
                    deserializeLocation(
                            config.getConfigurationSection(
                                    "bases." + name + ".spawn"
                            )
                    );

            if (pos1 != null) {
                base.setPos1(pos1);
            }

            if (pos2 != null) {
                base.setPos2(pos2);
            }

            if (spawn != null) {
                base.setSpawn(spawn);
            }

            bases.put(
                    name.toLowerCase(),
                    base
            );
        }
    }

    // =================================================
    // LOCATION SERIALIZATION
    // =================================================

    private Map<String, Object> serializeLocation(
            Location location
    ) {

        Map<String, Object> data =
                new HashMap<>();

        if (location == null) {
            return data;
        }

        data.put(
                "world",
                location.getWorld().getName()
        );

        data.put(
                "x",
                location.getX()
        );

        data.put(
                "y",
                location.getY()
        );

        data.put(
                "z",
                location.getZ()
        );

        data.put(
                "yaw",
                location.getYaw()
        );

        data.put(
                "pitch",
                location.getPitch()
        );

        return data;
    }

    private Location deserializeLocation(
            ConfigurationSection section
    ) {

        if (section == null) {
            return null;
        }

        String worldName =
                section.getString("world");

        World world =
                Bukkit.getWorld(worldName);

        if (world == null) {
            return null;
        }

        double x =
                section.getDouble("x");

        double y =
                section.getDouble("y");

        double z =
                section.getDouble("z");

        float yaw =
                (float) section.getDouble("yaw");

        float pitch =
                (float) section.getDouble("pitch");

        return new Location(
                world,
                x,
                y,
                z,
                yaw,
                pitch
        );
    }
        }
