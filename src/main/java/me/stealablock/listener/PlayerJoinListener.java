package me.stealablock.listener;

import me.stealablock.StealABlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    private final StealABlock plugin;

    public PlayerJoinListener(
            StealABlock plugin
    ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(
            PlayerJoinEvent event
    ) {

        Player player =
                event.getPlayer();

        boolean firstJoin =
                !plugin.getPlayerDataManager()
                        .hasPlayer(
                                player.getUniqueId()
                        );

        plugin.getPlayerDataManager()
                .createPlayer(
                        player.getUniqueId()
                );

        if (firstJoin) {
            giveEyeOfEnder(player);
        }

        plugin.getScoreboard()
                .create(player);
    }

    private void giveEyeOfEnder(
            Player player
    ) {

        ItemStack eye =
                new ItemStack(
                        Material.ENDER_EYE
                );

        ItemMeta meta =
                eye.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    "§5§lMain Menu"
            );

            meta.setLore(
                    java.util.List.of(
                            "§7Right click to open",
                            "§7the Steal A Block menu."
                    )
            );

            eye.setItemMeta(meta);
        }

        player.getInventory()
                .addItem(eye);
    }
}
