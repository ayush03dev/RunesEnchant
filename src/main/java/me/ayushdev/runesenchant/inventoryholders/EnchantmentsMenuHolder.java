package me.ayushdev.runesenchant.inventoryholders;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EnchantmentsMenuHolder implements InventoryHolder {

    Player p;
    public EnchantmentsMenuHolder(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}