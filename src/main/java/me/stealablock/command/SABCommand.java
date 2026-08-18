package me.stealablock.command;

import me.stealablock.StealABlock;
import me.stealablock.selection.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SABCommand implements CommandExecutor, TabCompleter, Listener {

    private final StealABlock plugin;

    public SABCommand(StealABlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    color(
                            plugin.getConfig().getString(
                                    "messages.player-only",
                                    "&cPlayers only."
                            )
                    )
            );

            return true;
        }

        String permission = plugin.getConfig().getString(
                "admin.permission",
                "stealablock.admin"
        );

        if (!player.hasPermission(permission)) {

            sendMessage(
                    player,
                    "messages.no-permission"
            );

            return true;
        }

        if (args.length == 0) {

            sendHelp(player);

            return true;
        }

        switch (args[0].toLowerCase()) {

            case "wand" -> giveWand(player);

            case "pos1" -> setPos1(player);

            case "pos2" -> setPos2(player);

            case "clear" -> clearSelection(player);

            case "reload" -> reloadConfig(player);

            default -> sendHelp(player);
        }

        return true;
    }

    // =================================================
    // HELP
    // =================================================

    private void sendHelp(Player player) {

        player.sendMessage("");
        player.sendMessage(
                color("&b&lSteal A Block")
        );
        player.sendMessage("");

        player.sendMessage(
                color("&e/sab wand &7- Get the selection wand")
        );

        player.sendMessage(
                color("&e/sab pos1 &7- Set position 1")
        );

        player.sendMessage(
                color("&e/sab pos2 &7- Set position 2")
        );

        player.sendMessage(
                color("&e/sab clear &7- Clear your selection")
        );

        player.sendMessage(
                color("&e/sab reload &7- Reload the configuration")
        );

        player.sendMessage("");
    }

    // =================================================
    // WAND
    // =================================================

    private void giveWand(Player player) {

        Material material = getWandMaterial();

        ItemStack wand = new ItemStack(material);

        ItemMeta meta = wand.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    color("&6&lStealABlock Wand")
            );

            List<String> lore = new ArrayList<>();

            lore.add(
                    color("&7Left Click &f→ &ePosition 1")
            );

            lore.add(
                    color("&7Right Click &f→ &ePosition 2")
            );

            meta.setLore(lore);

            wand.setItemMeta(meta);
        }

        player.getInventory().addItem(wand);

        sendMessage(
                player,
                "messages.wand-given"
        );
    }

    // =================================================
    // POS 1
    // =================================================

    private void setPos1(Player player) {

        Location location = getSelectionLocation(player);

        if (location == null) {
            return;
        }

        Selection selection =
                plugin.getSelectionManager()
                        .getSelection(player);

        selection.setPos1(location);

        sendMessage(
                player,
                "messages.pos1-set"
        );

        sendLocation(player, location);
    }

    // =================================================
    // POS 2
    // =================================================

    private void setPos2(Player player) {

        Location location = getSelectionLocation(player);

        if (location == null) {
            return;
        }

        Selection selection =
                plugin.getSelectionManager()
                        .getSelection(player);

        selection.setPos2(location);

        sendMessage(
                player,
                "messages.pos2-set"
        );

        sendLocation(player, location);

        if (selection.isComplete()) {

            sendMessage(
                    player,
                    "messages.selection-complete"
            );
        }
    }

    // =================================================
    // CLEAR
    // =================================================

    private void clearSelection(Player player) {

        plugin.getSelectionManager()
                .clearSelection(player);

        sendMessage(
                player,
                "messages.selection-cleared"
        );
    }

    // =================================================
    // RELOAD
    // =================================================

    private void reloadConfig(Player player) {

        plugin.reloadConfig();

        sendMessage(
                player,
                "messages.config-reloaded"
        );
    }

    // =================================================
    // INTERACTION
    // =================================================

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission(
                plugin.getConfig().getString(
                        "admin.permission",
                        "stealablock.admin"
                )
        )) {
            return;
        }

        if (!plugin.getConfig().getBoolean(
                "admin.selection.enabled",
                true
        )) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        Material wandMaterial = getWandMaterial();

        if (item.getType() != wandMaterial) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String expectedName =
                color("&6&lStealABlock Wand");

        if (!meta.getDisplayName().equals(expectedName)) {
            return;
        }

        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_BLOCK) {

            Location location =
                    getSelectionLocation(player);

            if (location != null) {

                Selection selection =
                        plugin.getSelectionManager()
                                .getSelection(player);

                selection.setPos1(location);

                sendMessage(
                        player,
                        "messages.pos1-set"
                );

                sendLocation(
                        player,
                        location
                );
            }

            cancelInteraction(event);
        }

        else if (action == Action.RIGHT_CLICK_BLOCK) {

            Location location =
                    getSelectionLocation(player);

            if (location != null) {

                Selection selection =
                        plugin.getSelectionManager()
                                .getSelection(player);

                selection.setPos2(location);

                sendMessage(
                        player,
                        "messages.pos2-set"
                );

                sendLocation(
                        player,
                        location
                );

                if (selection.isComplete()) {

                    sendMessage(
                            player,
                            "messages.selection-complete"
                    );
                }
            }

            cancelInteraction(event);
        }
    }

    // =================================================
    // LOCATION
    // =================================================

    private Location getSelectionLocation(Player player) {

        boolean useTarget =
                plugin.getConfig().getBoolean(
                        "selection.use-target-block",
                        true
                );

        int maxDistance =
                plugin.getConfig().getInt(
                        "selection.max-distance",
                        100
                );

        if (useTarget) {

            org.bukkit.block.Block block =
                    player.getTargetBlockExact(
                            maxDistance
                    );

            if (block == null) {

                player.sendMessage(
                        color(
                                "&cYou are not looking at a block."
                        )
                );

                return null;
            }

            return block.getLocation();
        }

        return player.getLocation().getBlock().getLocation();
    }

    // =================================================
    // LOCATION MESSAGE
    // =================================================

    private void sendLocation(
            Player player,
            Location location
    ) {

        if (plugin.getConfig().getBoolean(
                "selection.show-location",
                true
        )) {

            player.sendMessage(
                    color(
                            "&7X: &f" + location.getBlockX()
                                    + " &7Y: &f" + location.getBlockY()
                                    + " &7Z: &f" + location.getBlockZ()
                    )
            );
        }

        if (plugin.getConfig().getBoolean(
                "selection.show-world",
                true
        )) {

            player.sendMessage(
                    color(
                            "&7World: &f"
                                    + location.getWorld().getName()
                    )
            );
        }
    }

    // =================================================
    // WAND MATERIAL
    // =================================================

    private Material getWandMaterial() {

        String materialName =
                plugin.getConfig().getString(
                        "admin.selection.wand",
                        "GOLDEN_AXE"
                );

        try {

            return Material.valueOf(
                    materialName.toUpperCase()
            );

        } catch (IllegalArgumentException exception) {

            plugin.getLogger().warning(
                    "Invalid wand material: "
                            + materialName
            );

            return Material.GOLDEN_AXE;
        }
    }

    // =================================================
    // CANCEL INTERACTION
    // =================================================

    private void cancelInteraction(
            PlayerInteractEvent event
    ) {

        if (plugin.getConfig().getBoolean(
                "admin.selection.cancel-interaction",
                true
        )) {

            event.setCancelled(true);
        }
    }

    // =================================================
    // MESSAGE
    // =================================================

    private void sendMessage(
            Player player,
            String path
    ) {

        String prefix =
                plugin.getConfig().getString(
                        "messages.prefix",
                        ""
                );

        String message =
                plugin.getConfig().getString(
                        path,
                        "&cMessage not found."
                );

        player.sendMessage(
                color(prefix + message)
        );
    }

    // =================================================
    // COLOR
    // =================================================

    private String color(String text) {

        if (text == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes(
                '&',
                text
        );
    }

    // =================================================
    // TAB COMPLETE
    // =================================================

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (args.length == 1) {

            return List.of(
                    "wand",
                    "pos1",
                    "pos2",
                    "clear",
                    "reload"
            );
        }

        return List.of();
    }
              }
