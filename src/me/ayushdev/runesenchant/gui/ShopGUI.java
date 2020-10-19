package me.ayushdev.runesenchant.gui;

import me.ayushdev.runesenchant.EnchantmentGroup;
import me.ayushdev.runesenchant.inventoryholders.ShopGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShopGUI {

    public void openInventory(Player p) {
        int size = EnchantmentGroup.getAllGroupNames().size();
        int rows = ((size % 9) == 0) ? (size / 9) : ((size / 9) + 1);

        Inventory inv = Bukkit.createInventory(new ShopGUIHolder(p), rows * 9, "Runes Shop");

        int index = 0;
        for (EnchantmentGroup group : EnchantmentGroup.getAllGroups()) {
            if (group.listInShop()) {
                inv.setItem(index, group.getDisplayItem());
                index++;
            }
        }
//        EnchantmentGroup.getAllGroups().forEach(group -> {
//            if (group.listInShop()) {
//                inv.setItem(index, group.getDisplayItem());
//                index++;
//            }
//        });

        p.openInventory(inv);
    }
}
