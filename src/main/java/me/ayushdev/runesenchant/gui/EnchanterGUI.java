package me.ayushdev.runesenchant.gui;

import me.ayushdev.runesenchant.inventoryholders.GUIHolder;
import me.ayushdev.runesenchant.utils.RuneUtils;
import me.ayushdev.runesenchant.*;
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

    FileConfiguration fc;

    public EnchanterGUI() {
        this.fc = FileManager.getInstance().getEnchanterConfig();
    }

    public Inventory createEnchanterGUI(Player p) {
//        FileConfiguration fc = FileManager.getInstance().getEnchanterConfig();
        String title = ChatColor.translateAlternateColorCodes('&', fc.getString("gui-title"));

        Inventory inv = Bukkit.createInventory(new GUIHolder(p), 54, title);

        ItemStack resultant = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = resultant.getItemMeta();
        String name = ChatColor.translateAlternateColorCodes('&', FileManager.getInstance().getEnchanterConfig().getString(
                "display-names.not-ready-button"
        ));
//        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "NOT READY");
        meta.setDisplayName(name);
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
            glassMeta.setDisplayName(ChatColor.BLACK + "");
            glass.setItemMeta(glassMeta);

            inv.setItem(i, glass);
        }

        inv.setItem(19, getDemoItem());
        inv.setItem(21, getDemoRune());
        inv.setItem(23, getDemoResurrectionStone());
        inv.setItem(25, getDemoLuckStone());
        new EnchanterResultant(resultant).setErrorMessage(EnchanterItemMessage.DEFAULT);
        inv.setItem(40, resultant);

        return inv;
    }

    public ItemStack getDemoItem() {
        ItemStack demoItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = demoItem.getItemMeta();
        String name = ChatColor.translateAlternateColorCodes('&', fc.getString(
                "display-names.item"
        ));
//        itemMeta.setDisplayName(ChatColor.YELLOW + "Place item here");
        itemMeta.setDisplayName(name);

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        demoItem.setItemMeta(itemMeta);
        return demoItem;
    }

    public ItemStack getDemoRune() {
//        ItemStack demoRune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemStack demoRune = new ItemStack(Material.EMERALD);
        ItemMeta runeMeta = demoRune.getItemMeta();
        String name = ChatColor.translateAlternateColorCodes('&', fc.getString(
                "display-names.rune"
        ));
//        runeMeta.setDisplayName(ChatColor.YELLOW + "Place Rune here");
        runeMeta.setDisplayName(name);
        runeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        runeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        demoRune.setItemMeta(runeMeta);
        return demoRune;
    }

    public ItemStack getDemoResurrectionStone() {
        ItemStack demoResurrection = new ResurrectionStone().getItem();
        ItemMeta resurrectionItemMeta = demoResurrection.getItemMeta();
        String name = ChatColor.translateAlternateColorCodes('&', fc.getString(
                "display-names.resurrection"
        ));
//        resurrectionItemMeta.setDisplayName(ChatColor.YELLOW + "Place Resurrection Stone here");
        resurrectionItemMeta.setDisplayName(name);
        resurrectionItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        resurrectionItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        resurrectionItemMeta.setLore(null);
        demoResurrection.setItemMeta(resurrectionItemMeta);
        return demoResurrection;
    }

    public ItemStack getDemoLuckStone() {
        ItemStack demoLuck = new ItemStack(new LuckStone(1).getItem().getType());
        ItemMeta luckMeta = demoLuck.getItemMeta();
//        luckMeta.setDisplayName(ChatColor.YELLOW + "Place Luck Stone here");
        String name = ChatColor.translateAlternateColorCodes('&', fc.getString(
                "display-names.luck-stone"
        ));
        luckMeta.setDisplayName(name);
        luckMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        luckMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        luckMeta.setLore(null);
        demoLuck.setItemMeta(luckMeta);
        return demoLuck;
    }
}
