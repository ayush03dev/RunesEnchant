package me.ayush_03.runesenchant.gui;

import me.ayush_03.runesenchant.*;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchanterGUI {

    public Inventory createEnchanterGUI(Player p) {
        FileConfiguration fc = FileManager.getInstance().getEnchanterConfig();
        String title = ChatColor.translateAlternateColorCodes('&', fc.getString("gui-title"));

        Inventory inv = Bukkit.createInventory(new GUIHolder(p), 54, title);
        String demoString = HiddenStringUtils.encodeString("demo-item");

        ItemStack demoItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = demoItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        itemMeta.setDisplayName("Place item here" + demoString);
        demoItem.setItemMeta(itemMeta);

        ItemStack demoRune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemMeta runeMeta = demoRune.getItemMeta();
        runeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        runeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        runeMeta.setDisplayName("Place Rune here" + demoString);
        demoRune.setItemMeta(runeMeta);

        ItemStack demoResurrection = new ResurrectionStone().getItem();
        ItemMeta resurrectionItemMeta = demoResurrection.getItemMeta();
        resurrectionItemMeta.setDisplayName("Place the resurrection stone here" + demoString);
        resurrectionItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        resurrectionItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        resurrectionItemMeta.setLore(null);
        demoResurrection.setItemMeta(resurrectionItemMeta);

        ItemStack demoLuck = new ItemStack(new LuckStone(1).getItem().getType());
        ItemMeta luckMeta = demoLuck.getItemMeta();
        luckMeta.setDisplayName("Place the luck stone here" + demoString);
        luckMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        luckMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        luckMeta.setLore(null);
        demoLuck.setItemMeta(luckMeta);

        ItemStack resultant = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = resultant.getItemMeta();
        meta.setDisplayName("§a§lClick to Enchant");
        resultant.setItemMeta(meta);

        for (int i = 0; i < 54; i++) {
            Material mat;
            try {
                mat = Material.WHITE_STAINED_GLASS_PANE;
            } catch (NoSuchFieldError e) {
                mat = Material.matchMaterial("STAINED_GLASS_PANE");
            }

            ItemStack glass = new ItemStack(mat);
            ItemMeta glassMeta = glass.getItemMeta();
            glassMeta.setDisplayName(demoString);
            glass.setItemMeta(glassMeta);

            inv.setItem(i, glass);
        }

        inv.setItem(19, demoItem);
        inv.setItem(21, demoRune);
        inv.setItem(23, demoResurrection);
        inv.setItem(25, demoLuck);
        inv.setItem(40, resultant);

        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [I] [] [R] [] [P] [] [L] []
        // TODO: [] [] [] [] [] [] [] [] []
        // TODO: [] [] [] [] [Item] [] [] [] []
        // TODO: [] [] [] [] [] [] [] [] []
        return inv;
    }

}
