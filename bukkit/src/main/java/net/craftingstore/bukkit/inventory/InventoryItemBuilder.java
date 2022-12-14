package net.craftingstore.bukkit.inventory;

import com.cryptomorin.xseries.XMaterial;
import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.util.VersionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class InventoryItemBuilder {

    private final CraftingStoreBukkit instance;

    public InventoryItemBuilder(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    public ItemStack getItemStack(String material, int amount) {
        ItemStack itemStack;
        if (VersionUtil.isGuavaAvailable()) {
            // 1.8+
            com.cryptomorin.xseries.XMaterial xMaterial;
            if (material == null) {
                xMaterial = com.cryptomorin.xseries.XMaterial.CHEST;
            } else {
                Optional<XMaterial> xMaterialOptional = com.cryptomorin.xseries.XMaterial.matchXMaterial(material);
                if (!xMaterialOptional.isPresent()) {
                    instance.getCraftingStore().getLogger().debug("Material " + material + " not found.");
                    xMaterial = com.cryptomorin.xseries.XMaterial.CHEST;
                } else {
                    xMaterial = xMaterialOptional.get();
                }
            }
            itemStack = xMaterial.parseItem();
            if (itemStack == null) {
                itemStack = new ItemStack(Material.CHEST);
            }
            itemStack.setAmount(amount);
        } else {
            // 1.7 support
            net.craftingstore.bukkit.util.XMaterial xMaterial;
            if (material == null) {
                xMaterial = net.craftingstore.bukkit.util.XMaterial.CHEST;
            } else {
                xMaterial = net.craftingstore.bukkit.util.XMaterial.fromString(material);
                if (xMaterial == null || xMaterial.parseMaterial() == null) {
                    instance.getCraftingStore().getLogger().debug("Material " + material + " not found.");
                    xMaterial = net.craftingstore.bukkit.util.XMaterial.CHEST;
                }
            }
            itemStack = xMaterial.parseItem(amount);
        }
        return itemStack;
    }
}
