package me.stealablock.base;

import org.bukkit.Location;

import java.util.UUID;

public class Base {

    private final String name;

    private Location pos1;
    private Location pos2;
    private Location spawn;

    // Base owner
    private UUID owner;

    // Lock state
    private boolean locked = true;

    public Base(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // =========================
    // POSITIONS
    // =========================

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1 == null ? null : pos1.clone();
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2 == null ? null : pos2.clone();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn == null ? null : spawn.clone();
    }

    public boolean hasSelection() {
        return pos1 != null && pos2 != null;
    }

    public boolean hasSpawn() {
        return spawn != null;
    }

    // =========================
    // OWNER
    // =========================

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    // =========================
    // LOCK
    // =========================

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
        }
