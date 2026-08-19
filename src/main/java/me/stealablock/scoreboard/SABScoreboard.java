package me.stealablock.scoreboard;

import me.stealablock.StealABlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

public class SABScoreboard {

    private final StealABlock plugin;

    private final DecimalFormat moneyFormat =
            new DecimalFormat("#,###");

    public SABScoreboard(
            StealABlock plugin
    ) {
        this.plugin = plugin;
    }

    public void create(Player player) {

        Scoreboard scoreboard =
                Bukkit.getScoreboardManager()
                        .getNewScoreboard();

        Objective objective =
                scoreboard.registerNewObjective(
                        "stealablock",
                        "dummy",
                        color("&5&lSTEAL A BLOCK")
                );

        objective.setDisplaySlot(
                DisplaySlot.SIDEBAR
        );

        player.setScoreboard(
                scoreboard
        );

        update(player);
    }

    public void update(Player player) {

        Scoreboard scoreboard =
                player.getScoreboard();

        Objective objective =
                scoreboard.getObjective(
                        "stealablock"
                );

        if (objective == null) {
            create(player);
            return;
        }

        scoreboard.getEntries()
                .forEach(scoreboard::resetScores);

        String name =
                player.getName();

        double money =
                plugin.getMoneyManager()
                        .getMoney(player);

        int rebirth =
                plugin.getRebirthManager()
                        .getRebirth(player);

        int prestige =
                plugin.getPrestigeManager()
                        .getPrestige(player);

        objective.getScore(
                color("&7&m----------------")
        ).setScore(9);

        objective.getScore(
                color("&fPlayer:")
        ).setScore(8);

        objective.getScore(
                color("&d" + name)
        ).setScore(7);

        objective.getScore(
                color("&r")
        ).setScore(6);

        objective.getScore(
                color("&fMoney:")
        ).setScore(5);

        objective.getScore(
                color("&a$"
                        + moneyFormat.format(money))
        ).setScore(4);

        objective.getScore(
                color("&r ")
        ).setScore(3);

        objective.getScore(
                color("&fRebirth: &d"
                        + rebirth
                        + "&7/40")
        ).setScore(2);

        objective.getScore(
                color("&fPrestige: &6"
                        + prestige)
        ).setScore(1);
    }

    public void updateAll() {

        for (Player player :
                Bukkit.getOnlinePlayers()) {

            update(player);
        }
    }

    private String color(String text) {

        return ChatColor.translateAlternateColorCodes(
                '&',
                text
        );
    }
          }
