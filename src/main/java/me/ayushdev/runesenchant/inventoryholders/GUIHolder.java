package me.ayushdev.runesenchant.inventoryholders;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {

    private final Player p;

    public GUIHolder(Player p) {
        this.p = p;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public Player getPlayer() {
        return p;
    }
}
