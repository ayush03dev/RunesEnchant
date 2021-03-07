package me.ayushdev.runesenchant.inventoryholders;

import me.ayushdev.runesenchant.EnchantmentGroup;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EnchantmentsGUIHolder implements InventoryHolder {

    Player p;
    int currentPage;
    EnchantmentGroup group;

    public EnchantmentsGUIHolder(Player p, EnchantmentGroup group, int currentPage) {
        this.p = p;
        this.group = group;
        this.currentPage = currentPage;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public Player getPlayer() {
        return p;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public EnchantmentGroup getGroup() {
        return group;
    }
}
