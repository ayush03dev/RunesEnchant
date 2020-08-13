package me.ayush_03.runesenchant.gui;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.GUIHolder;
import me.ayush_03.runesenchant.Settings;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchanterGUI {

    public Inventory createEnchanterGUI(Player p) {
        FileConfiguration fc = FileManager.getInstance().getEnchanterConfig();
        String title = ChatColor.translateAlternateColorCodes('&', fc.getString("gui-title"));

        Inventory inv = Bukkit.createInventory(new GUIHolder(p), 45, title);

        ItemStack demoItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = demoItem.getItemMeta();
        itemMeta.setDisplayName("Place item here" + HiddenStringUtils.encodeString("demo-item"));
        demoItem.setItemMeta(itemMeta);

        ItemStack demoRune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemMeta runeMeta = demoRune.getItemMeta();
        runeMeta.setDisplayName("Place Rune here" + HiddenStringUtils.encodeString("demo-item"));
        demoItem.setItemMeta(runeMeta);

        inv.setItem(19, demoItem);
        inv.setItem(21, demoRune);

        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [I] [] [R] [] [P] [] [L] []
        // TODO: [] [] [] [] [Item] [] [] [] []
        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [] [] [] [] [] [] [] []
        return inv;
    }

}
