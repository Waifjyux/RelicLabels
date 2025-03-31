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

    @EventHandler
    public void onItemHandling(InventoryClickEvent e) {

        if(!BaseConfig.updateOnEnchant) return;

        ItemStack i = e.getCurrentItem();
        if(i == null) return;
        if(e.getInventory().getType() != InventoryType.ANVIL &&
        e.getInventory().getType() != InventoryType.ENCHANTING &&
        e.getInventory().getType() != InventoryType.GRINDSTONE) return;

        RelicLabel.updateItemStack(i);

    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if(!BaseConfig.updateOnPickup) return;
        ItemStack i = e.getItem().getItemStack();
        RelicLabel.updateItemStack(i);
    }

    @EventHandler
    public void onItemJoin(PlayerJoinEvent e) {
        if(!BaseConfig.updateOnJoin) return;
        for(ItemStack i : e.getPlayer().getInventory().getContents()) {
            RelicLabel.updateItemStack(i);
        }
    }



}
