package de.waifjyux.relicLabels.events;

import de.waifjyux.relicLabels.labels.RelicLabel;
import de.waifjyux.relicLabels.util.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class RelicLabelEvents implements Listener {

    // Event handler for item interactions (clicking items in an inventory)
    @EventHandler
    public void onItemHandling(InventoryClickEvent e) {

        // Check if item updates on enchantments are enabled in the config
        if(!BaseConfig.updateOnEnchant) return;

        // Get the current item being clicked
        ItemStack i = e.getCurrentItem();
        // If the item is null, do nothing
        if(i == null) return;

        // Check if the inventory is one of the supported types (Anvil, Enchanting, Grindstone)
        if(e.getInventory().getType() != InventoryType.ANVIL &&
                e.getInventory().getType() != InventoryType.ENCHANTING &&
                e.getInventory().getType() != InventoryType.GRINDSTONE) return;

        // Update the item stack (relic label update)
        RelicLabel.updateItemStack(i);
    }

    // Event handler for picking up items
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        // Check if item updates on pickup are enabled in the config
        if(!BaseConfig.updateOnPickup) return;

        // Get the item being picked up
        ItemStack i = e.getItem().getItemStack();
        // Update the item stack (relic label update)
        RelicLabel.updateItemStack(i);
    }

    // Event handler for when a player joins the game
    @EventHandler
    public void onItemJoin(PlayerJoinEvent e) {
        // Check if item updates on player join are enabled in the config
        if(!BaseConfig.updateOnJoin) return;

        // Iterate through all items in the player's inventory and update them
        for(ItemStack i : e.getPlayer().getInventory().getContents()) {
            RelicLabel.updateItemStack(i);
        }
    }
}
