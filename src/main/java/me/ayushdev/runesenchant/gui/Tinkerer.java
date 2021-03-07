package me.ayushdev.runesenchant.gui;

import me.ayushdev.runesenchant.RunesEnchant;
import me.ayushdev.runesenchant.inventoryholders.TinkererHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Tinkerer {

    //  0            4            8
    // [x] [] [] [] [x] [] [] [] [x]

    public void openInventory(Player p) {
        Inventory inv = Bukkit.createInventory(new TinkererHolder(p), 54, "Tinkerer");

        ItemStack item;

        if (RunesEnchant.is13()) {
            item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        } else {
            item = new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte) 14);
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK + "");
        item.setItemMeta(meta);

        inv.setItem(0, item);
        inv.setItem(8, item);

        ItemStack glass;
        if (RunesEnchant.is13()) {
            glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        } else {
            glass = new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"));
        }

        ItemMeta gmeta =  glass.getItemMeta();
        gmeta.setDisplayName(ChatColor.BLACK + "");
        glass.setItemMeta(gmeta);

        for (int i = 4; i <= 49; i = i + 9) {
            inv.setItem(i, glass);
        }

        p.openInventory(inv);
    }

}
