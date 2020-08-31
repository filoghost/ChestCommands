/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import me.filoghost.fcommons.Preconditions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public enum VaultEconomyHook implements PluginHook {

    INSTANCE;
    
    private Economy economy;

    @Override
    public void setup() {
        economy = null;
        
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        
        RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyServiceProvider == null) {
            return;
        }
        
        economy = economyServiceProvider.getProvider();
    }

    @Override
    public boolean isEnabled() {
        return economy != null;
    }

    public static double getMoney(Player player) {
        INSTANCE.checkEnabledState();
        return INSTANCE.economy.getBalance(player, player.getWorld().getName());
    }

    public static boolean hasMoney(Player player, double minimum) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(minimum);

        double balance = INSTANCE.economy.getBalance(player, player.getWorld().getName());
        return balance >= minimum;
    }

    /*
     * Returns true if the operation was successful.
     */
    public static boolean takeMoney(Player player, double amount) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(amount);

        EconomyResponse response = INSTANCE.economy.withdrawPlayer(player, player.getWorld().getName(), amount);
        return response.transactionSuccess();
    }

    public static boolean giveMoney(Player player, double amount) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(amount);

        EconomyResponse response = INSTANCE.economy.depositPlayer(player, player.getWorld().getName(), amount);
        return response.transactionSuccess();
    }
    
    private static void checkPositiveAmount(double amount) {
        Preconditions.checkArgument(amount >= 0.0, "amount cannot be negative");
    }

    public static String formatMoney(double amount) {
        if (INSTANCE.economy != null) {
            return INSTANCE.economy.format(amount);
        } else {
            return Double.toString(amount);
        }
    }
}
