package net.craftingstore.nukkit;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.utils.TextFormat;
import com.nukkitx.fakeinventories.inventory.FakeInventories;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.nukkit.commands.BuyCommand;
import net.craftingstore.nukkit.commands.CraftingStoreCommand;
import net.craftingstore.nukkit.config.Config;
import net.craftingstore.nukkit.listeners.AdminJoinListener;
import net.craftingstore.nukkit.listeners.InventoryListener;
import net.craftingstore.nukkit.listeners.PendingDonationJoinListener;
import org.simpleyaml.configuration.file.FileConfiguration;

public class CraftingStoreNukkit extends PluginBase {
    private CraftingStore craftingStore;
    private Config config;
    private String prefix = TextFormat.GRAY + "[" + TextFormat.RED + "CraftingStore" + TextFormat.GRAY + "] " + TextFormat.WHITE;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<FakeInventories> provider = getServer().getServiceManager().getProvider(FakeInventories.class);
        if (provider == null || provider.getProvider() == null) {

        }
        config = new Config("config.yml", this);
        this.craftingStore = new CraftingStore(new CraftingStoreNukkitImpl(this));

        PluginCommand cs = (PluginCommand) getServer().getPluginCommand("craftingstore");
        cs.setExecutor(new CraftingStoreCommand(this));
        if (this.craftingStore.getImplementation().getConfiguration().isBuyCommandEnabled()) {
            PluginCommand buy = (PluginCommand) getServer().getPluginCommand("csbuy");
            buy.setExecutor(new BuyCommand(this));
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new AdminJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PendingDonationJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        craftingStore.setEnabled(false);
        craftingStore.getLogger().debug("Shutdown complete");
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public FileConfiguration getConfigFile() {
        return this.config.getConfig();
    }

    public Config getConfigWrapper() {
        return this.config;
    }

    public String getPrefix() {
        return prefix;
    }
}