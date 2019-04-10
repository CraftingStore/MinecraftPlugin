package net.craftingstore.sponge.commands;

import com.google.inject.Inject;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.sponge.CraftingStoreSponge;
import net.craftingstore.sponge.inventory.InventoryBuilder;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;

public class BuyCommand implements CommandExecutor {

    @Inject
    private CraftingStore craftingStore;

    @Inject
    private CraftingStoreSponge spongePlugin;

    @Inject
    private Game game;

    @Inject
    private InventoryBuilder inventoryBuilder;

    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("You can only execute this command as a player"));
        }
        Player p = (Player) src;

        game.getScheduler().createTaskBuilder()
                .execute(() -> {
                    try {
                        CraftingStoreInventory gui = craftingStore.getApi().getGUI().get();
                        Inventory inventory = this.inventoryBuilder.buildInventory(gui);
                        game.getScheduler().createTaskBuilder()
                                .execute(() -> {
                                    if (p.isOnline()) {
                                        p.openInventory(inventory);
                                    }
                                })
                                .submit(spongePlugin);
                    } catch (InterruptedException | ExecutionException | CraftingStoreApiException e) {
                        e.printStackTrace();
                    }
                })
                .async()
                .submit(spongePlugin);

        return CommandResult.success();
    }
}
