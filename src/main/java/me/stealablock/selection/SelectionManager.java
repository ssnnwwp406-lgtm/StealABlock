package me.stealablock.selection;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {

    private final Map<UUID, Selection> selections = new HashMap<>();

    public Selection getSelection(Player player) {

        return selections.computeIfAbsent(
                player.getUniqueId(),
                uuid -> new Selection()
        );
    }

    public void clearSelection(Player player) {

        selections.remove(player.getUniqueId());
    }
}
