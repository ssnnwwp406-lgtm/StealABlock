package me.stealablock.command;

import me.stealablock.StealABlock;
import me.stealablock.base.Base;
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

public class SABCommand
        implements CommandExecutor, TabCompleter, Listener {

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
                    "This command can only be used by players."
            );

            return true;
        }

        String permission =
                plugin.getConfig().getString(
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

            case "setbase" -> {

                if (args.length < 2) {

                    player.sendMessage(
                            color(
                                    "&cUsage: /sab setbase <name>"
                            )
                    );

                    return true;
                }

                setBase(
                        player,
                        args[1]
                );
            }

            case "setspawn" -> {

                if (args.length < 2) {

                    player.sendMessage(
                            color(
                                    "&cUsage: /sab setspawn <base>"
                            )
                    );

                    return true;
                }

                setSpawn(
                        player,
                        args[1]
                );
            }

            case "base" -> handleBaseCommand(
                    player,
                    args
            );

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
                color("&e/sab setbase <name> &7- Create a base")
        );

        player.sendMessage(
                color("&e/sab setspawn <base> &7- Set block spawn")
        );

        player.sendMessage(
                color("&e/sab base list &7- List all bases")
        );

        player.sendMessage(
                color("&e/sab base info <name> &7- Base information")
        );

        player.sendMessage(
                color("&e/sab base delete <name> &7- Delete a base")
        );

        player.sendMessage(
                color("&e/sab clear &7- Clear selection")
        );

        player.sendMessage(
                color("&e/sab reload &7- Reload config")
        );

        player.sendMessage("");
    }

    // =================================================
    // SET BASE
    // =================================================

    private void setBase(
            Player player,
            String name
    ) {

        Selection selection =
                plugin.getSelectionManager()
                        .getSelection(player);

        if (!selection.isComplete()) {

            player.sendMessage(
                    color(
                            "&cYou must set both Pos1 and Pos2 first."
                    )
            );

            return;
        }

        if (plugin.getBaseManager()
                .getBase(name) != null) {

            player.sendMessage(
                    color(
                            "&cA base with this name already exists."
                    )
            );

            return;
        }

        boolean created =
                plugin.getBaseManager()
                        .createBase(
                                name,
                                selection.getPos1(),
                                selection.getPos2()
                        );

        if (!created) {

            player.sendMessage(
                    color(
                            "&cFailed to create the base."
                    )
            );

            return;
        }

        player.sendMessage(
                color(
                        "&a✔ Base &e" +
                                name +
                                " &ahas been created!"
                )
        );

        player.sendMessage(
                color(
                        "&7Now set its block spawn using:"
                )
        );

        player.sendMessage(
                color(
                        "&e/sab setspawn " + name
                )
        );
    }

    // =================================================
    // SET SPAWN
    // =================================================

    private void setSpawn(
            Player player,
            String baseName
    ) {

        Base base =
                plugin.getBaseManager()
                        .getBase(baseName);

        if (base == null) {

            player.sendMessage(
                    color(
                            "&cBase not found: &e"
                                    + baseName
                    )
            );

            return;
        }

        Location location =
                player.getLocation();

        boolean success =
                plugin.getBaseManager()
                        .setSpawn(
                                baseName,
                                location
                        );

        if (!success) {

            player.sendMessage(
                    color(
                            "&cFailed to set spawn."
                    )
            );

            return;
        }

        player.sendMessage(
                color(
                        "&a✔ Block spawn for base &e"
                                + baseName
                                + " &ahas been set!"
                )
        );

        sendLocation(
                player,
                location
        );
    }

    // =================================================
    // BASE COMMAND
    // =================================================

    private void handleBaseCommand(
            Player player,
            String[] args
    ) {

        if (args.length < 2) {

            player.sendMessage(
                    color(
                            "&cUsage: /sab base <list|info|delete>"
                    )
            );

            return;
        }

        switch (args[1].toLowerCase()) {

            case "list" -> listBases(player);

            case "info" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            color(
                                    "&cUsage: /sab base info <name>"
                            )
                    );

                    return;
                }

                baseInfo(
                        player,
                        args[2]
                );
            }

            case "delete" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            color(
                                    "&cUsage: /sab base delete <name>"
                            )
                    );

                    return;
                }

                deleteBase(
                        player,
                        args[2]
                );
            }

            default -> player.sendMessage(
                    color(
                            "&cUnknown base command."
                    )
            );
        }
    }

    // =================================================
    // LIST BASES
    // =================================================

    private void listBases(Player player) {

        player.sendMessage("");

        player.sendMessage(
                color("&b&lBases")
        );

        if (plugin.getBaseManager()
                .getBases()
                .isEmpty()) {

            player.sendMessage(
                    color("&7No bases have been created.")
            );

            player.sendMessage("");

            return;
        }

        for (Base base :
                plugin.getBaseManager().getBases()) {

            String spawnStatus =
                    base.hasSpawn()
                            ? "&aSet"
                            : "&cNot Set";

            player.sendMessage(
                    color(
                            "&7- &e"
                                    + base.getName()
                                    + " &7| Spawn: "
                                    + spawnStatus
                    )
            );
        }

        player.sendMessage("");
    }

    // =================================================
    // BASE INFO
    // =================================================

    private void baseInfo(
            Player player,
            String name
    ) {

        Base base =
                plugin.getBaseManager()
                        .getBase(name);

        if (base == null) {

            player.sendMessage(
                    color(
                            "&cBase not found."
                    )
            );

            return;
        }

        player.sendMessage("");

        player.sendMessage(
                color(
                        "&b&lBase: &e"
                                + base.getName()
                )
        );

        player.sendMessage("");

        player.sendMessage(
                color(
                        "&7Pos1: "
                                + formatLocation(
                                base.getPos1()
                        )
                )
        );

        player.sendMessage(
                color(
                        "&7Pos2: "
                                + formatLocation(
                                base.getPos2()
                        )
                )
        );

        player.sendMessage(
                color(
                        "&7Spawn: "
                                + (base.hasSpawn()
                                ? formatLocation(
                                base.getSpawn()
                        )
                                : "&cNot Set")
                )
        );

        player.sendMessage("");
    }

    // =================================================
    // DELETE BASE
    // =================================================

    private void deleteBase(
            Player player,
            String name
    ) {

        boolean deleted =
                plugin.getBaseManager()
                        .deleteBase(name);

        if (!deleted) {

            player.sendMessage(
                    color(
                            "&cBase not found."
                    )
            );

            return;
        }

        player.sendMessage(
                color(
                        "&a✔ Base &e"
                                + name
                                + " &ahas been deleted."
                )
        );
    }

    // =================================================
    // WAND
    // =================================================

    private void giveWand(Player player) {

        Material material =
                getWandMaterial();

        ItemStack wand =
                new ItemStack(material);

        ItemMeta meta =
                wand.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    color(
                            "&6&lStealABlock Wand"
                    )
            );

            List<String> lore =
                    new ArrayList<>();

            lore.add(
                    color(
                            "&7Left Click &f→ &ePos1"
                    )
            );

            lore.add(
                    color(
                            "&7Right Click &f→ &ePos2"
                    )
            );

            meta.setLore(lore);

            wand.setItemMeta(meta);
        }

        player.getInventory()
                .addItem(wand);

        sendMessage(
                player,
                "messages.wand-given"
        );
    }

    // =================================================
    // POS 1
    // =================================================

    private void setPos1(Player player) {

        Location location =
                getSelectionLocation(player);

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

        sendLocation(
                player,
                location
        );
    }

    // =================================================
    // POS 2
    // =================================================

    private void setPos2(Player player) {

        Location location =
                getSelectionLocation(player);

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
    public void onInteract(
            PlayerInteractEvent event
    ) {

        Player player =
                event.getPlayer();

        String permission =
                plugin.getConfig().getString(
                        "admin.permission",
                        "stealablock.admin"
                );

        if (!player.hasPermission(permission)) {
            return;
        }

        if (!plugin.getConfig()
                .getBoolean(
                        "admin.selection.enabled",
                        true
                )) {
            return;
        }

        ItemStack item =
                event.getItem();

        if (item == null) {
            return;
        }

        if (item.getType()
                != getWandMaterial()) {
            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null ||
                !meta.hasDisplayName()) {
            return;
        }

        if (!meta.getDisplayName()
                .equals(
                        color(
                                "&6&lStealABlock Wand"
                        )
                )) {
            return;
        }

        Action action =
                event.getAction();

        if (action ==
                Action.LEFT_CLICK_BLOCK) {

            setPos1(player);

            cancelInteraction(event);
        }

        else if (action ==
                Action.RIGHT_CLICK_BLOCK) {

            setPos2(player);

            cancelInteraction(event);
        }
    }

    // =================================================
    // LOCATION
    // =================================================

    private Location getSelectionLocation(
            Player player
    ) {

        boolean useTarget =
                plugin.getConfig()
                        .getBoolean(
                                "selection.use-target-block",
                                true
                        );

        int maxDistance =
                plugin.getConfig()
                        .getInt(
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

        return player.getLocation()
                .getBlock()
                .getLocation();
    }

    // =================================================
    // LOCATION MESSAGE
    // =================================================

    private void sendLocation(
            Player player,
            Location location
    ) {

        if (plugin.getConfig()
                .getBoolean(
                        "selection.show-location",
                        true
                )) {

            player.sendMessage(
                    color(
                            "&7X: &f"
                                    + location.getBlockX()
                                    + " &7Y: &f"
                                    + location.getBlockY()
                                    + " &7Z: &f"
                                    + location.getBlockZ()
                    )
            );
        }

        if (plugin.getConfig()
                .getBoolean(
                        "selection.show-world",
                        true
                )) {

            player.sendMessage(
                    color(
                            "&7World: &f"
                                    + location.getWorld()
                                    .getName()
                    )
            );
        }
    }

    // =================================================
    // FORMAT LOCATION
    // =================================================

    private String formatLocation(
            Location location
    ) {

        if (location == null) {
            return "&cNone";
        }

        return "&f"
                + location.getBlockX()
                + ", "
                + location.getBlockY()
                + ", "
                + location.getBlockZ();
    }

    // =================================================
    // WAND MATERIAL
    // =================================================

    private Material getWandMaterial() {

        String materialName =
                plugin.getConfig()
                        .getString(
                                "admin.selection.wand",
                                "GOLDEN_AXE"
                        );

        try {

            return Material.valueOf(
                    materialName.toUpperCase()
            );

        } catch (IllegalArgumentException exception) {

            return Material.GOLDEN_AXE;
        }
    }

    // =================================================
    // CANCEL INTERACTION
    // =================================================

    private void cancelInteraction(
            PlayerInteractEvent event
    ) {

        if (plugin.getConfig()
                .getBoolean(
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
                plugin.getConfig()
                        .getString(
                                "messages.prefix",
                                ""
                        );

        String message =
                plugin.getConfig()
                        .getString(
                                path,
                                "&cMessage not found."
                        );

        player.sendMessage(
                color(
                        prefix + message
                )
        );
    }

    // =================================================
    // COLOR
    // =================================================

    private String color(String text) {

        return ChatColor
                .translateAlternateColorCodes(
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
                    "setbase",
                    "setspawn",
                    "base",
                    "clear",
                    "reload"
            );
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase(
                    "base"
            )) {

                return List.of(
                        "list",
                        "info",
                        "delete"
                );
            }

            if (args[0].equalsIgnoreCase(
                    "setspawn"
            )) {

                return plugin.getBaseManager()
                        .getBases()
                        .stream()
                        .map(Base::getName)
                        .toList();
            }

            if (args[0].equalsIgnoreCase(
                    "setbase"
            )) {

                return List.of(
                        "<name>"
                );
            }
        }

        if (args.length == 3 &&
                args[0].equalsIgnoreCase(
                        "base"
                )) {

            if (args[1].equalsIgnoreCase(
                    "info"
            ) ||
                    args[1].equalsIgnoreCase(
                            "delete"
                    )) {

                return plugin.getBaseManager()
                        .getBases()
                        .stream()
                        .map(Base::getName)
                        .toList();
            }
        }

        return List.of();
    }
            }
