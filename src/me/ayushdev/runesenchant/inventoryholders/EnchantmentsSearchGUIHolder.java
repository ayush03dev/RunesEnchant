package me.ayushdev.runesenchant.inventoryholders;

import me.ayushdev.runesenchant.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class EnchantmentsSearchGUIHolder implements InventoryHolder {

    Player p;
    int currentPage;
    List<CustomEnchant> list;

    public EnchantmentsSearchGUIHolder(Player p, List<CustomEnchant> list, int currentPage) {
        this.p = p;
        this.list = list;
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

    public List<CustomEnchant> getList() {
        return list;
    }
}
