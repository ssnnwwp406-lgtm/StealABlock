package me.stealablock.economy;

import me.stealablock.StealABlock;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyManager {

    private final StealABlock plugin;

    public MoneyManager(StealABlock plugin) {
        this.plugin = plugin;
    }

    public double getMoney(Player player) {
        return getMoney(player.getUniqueId());
    }

    public double getMoney(UUID uuid) {
        return plugin.getPlayerDataManager()
                .getMoney(uuid);
    }

    public void setMoney(
            Player player,
            double amount
    ) {
        plugin.getPlayerDataManager()
                .setMoney(
                        player.getUniqueId(),
                        amount
                );
    }

    public void addMoney(
            Player player,
            double amount
    ) {
        plugin.getPlayerDataManager()
                .addMoney(
                        player.getUniqueId(),
                        amount
                );
    }

    public boolean hasMoney(
            Player player,
            double amount
    ) {
        return getMoney(player) >= amount;
    }

    public boolean removeMoney(
            Player player,
            double amount
    ) {

        if (!hasMoney(player, amount)) {
            return false;
        }

        setMoney(
                player,
                getMoney(player) - amount
        );

        return true;
    }

    public boolean withdraw(
            Player player,
            double amount
    ) {
        return removeMoney(
                player,
                amount
        );
    }
}
