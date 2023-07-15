package com.github.axescode.core.economy;

import com.github.axescode.container.Containers;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class AxesEconomy implements Economy {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        return Containers.getPlayerDataContainer().getData(playerName).getPlayerMoney();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return Containers.getPlayerDataContainer().getData(player.getName()).getPlayerMoney();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return Containers.getPlayerDataContainer().getData(playerName).getPlayerMoney();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return Containers.getPlayerDataContainer().getData(player.getName()).getPlayerMoney();
    }

    @Override
    public boolean has(String playerName, double amount) {
        return this.getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return this.getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.getBalance(playerName, worldName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return this.getBalance(player, worldName) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Containers.getPlayerDataContainer().getData(playerName).setPlayerMoney((long) (this.getBalance(playerName) - amount));
        return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Containers.getPlayerDataContainer().getData(player.getName()).setPlayerMoney((long) (this.getBalance(player) - amount));
        return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        Containers.getPlayerDataContainer().getData(playerName).setPlayerMoney((long) (this.getBalance(playerName, worldName) - amount));
        return new EconomyResponse(amount, this.getBalance(playerName, worldName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        Containers.getPlayerDataContainer().getData(player.getName()).setPlayerMoney((long) (this.getBalance(player, worldName) - amount));
        return new EconomyResponse(amount, this.getBalance(player, worldName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Containers.getPlayerDataContainer().getData(playerName).setPlayerMoney((long) (this.getBalance(playerName) + amount));
        return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Containers.getPlayerDataContainer().getData(player.getName()).setPlayerMoney((long) (this.getBalance(player) + amount));
        return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        Containers.getPlayerDataContainer().getData(playerName).setPlayerMoney((long) (this.getBalance(playerName, worldName) + amount));
        return new EconomyResponse(amount, this.getBalance(playerName, worldName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        Containers.getPlayerDataContainer().getData(player.getName()).setPlayerMoney((long) (this.getBalance(player, worldName) + amount));
        return new EconomyResponse(amount, this.getBalance(player, worldName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

}
