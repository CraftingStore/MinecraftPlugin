package net.craftingstore.bukkit.hooks;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private final CraftingStoreBukkit instance;
    private Economy economy;

    public VaultHook(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    public boolean register() {
        RegisteredServiceProvider<Economy> rsp = instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return economy;
    }
}
