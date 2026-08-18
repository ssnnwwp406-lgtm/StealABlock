package me.stealablock.base;

import org.bukkit.Location;

public class Base {

    private final String name;

    private Location pos1;
    private Location pos2;
    private Location spawn;

    public Base(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1.clone();
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2.clone();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn.clone();
    }

    public boolean hasSelection() {
        return pos1 != null && pos2 != null;
    }

    public boolean hasSpawn() {
        return spawn != null;
    }
}
