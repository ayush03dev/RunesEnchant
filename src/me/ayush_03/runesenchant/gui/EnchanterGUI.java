package me.ayush_03.runesenchant.gui;

import me.ayush_03.runesenchant.*;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EnchanterGUI {

    private final String demoString = HiddenStringUtils.encodeString("demo-item");

    public Inventory createEnchanterGUI(Player p) {
        FileConfiguration fc = FileManager.getInstance().getEnchanterConfig();
        String title = ChatColor.translateAlternateColorCodes('&', fc.getString("gui-title"));

        Inventory inv = Bukkit.createInventory(new GUIHolder(p), 54, title);

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

            if (RunesEnchant.is13()) {
                createDemoData("re.glass", glassMeta);
                glassMeta.setDisplayName(ChatColor.BLACK + "");
            } else {
                glassMeta.setDisplayName(HiddenStringUtils.encodeString("glass"));
            }
            glass.setItemMeta(glassMeta);

            inv.setItem(i, glass);
        }

        inv.setItem(19, getDemoItem());
        inv.setItem(21, getDemoRune());
        inv.setItem(23, getDemoResurrectionStone());
        inv.setItem(25, getDemoLuckStone());
        inv.setItem(40, resultant);

        return inv;
    }

    public ItemStack getDemoItem() {
        ItemStack demoItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = demoItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        if (RunesEnchant.is13()) {
            createDemoData("re.demo",itemMeta);
            itemMeta.setDisplayName("Place item here");
        } else {
            itemMeta.setDisplayName("Place item here" + demoString);
        }
        demoItem.setItemMeta(itemMeta);
        return demoItem;
    }

    public ItemStack getDemoRune() {
        ItemStack demoRune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemMeta runeMeta = demoRune.getItemMeta();
        runeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        runeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        if (RunesEnchant.is13()) {
            createDemoData("re.demo", runeMeta);
            runeMeta.setDisplayName("Place Rune here");
        } else {
            runeMeta.setDisplayName("Place Rune here" + demoString);
        }
        demoRune.setItemMeta(runeMeta);
        return demoRune;
    }

    public ItemStack getDemoResurrectionStone() {
        ItemStack demoResurrection = new ResurrectionStone().getItem();
        ItemMeta resurrectionItemMeta = demoResurrection.getItemMeta();

        if (RunesEnchant.is13()) {
            createDemoData("re.demo", resurrectionItemMeta);
            resurrectionItemMeta.setDisplayName("Place the resurrection stone here");
        } else {
            resurrectionItemMeta.setDisplayName("Place the resurrection stone here" + demoString);
        }
        resurrectionItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        resurrectionItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        resurrectionItemMeta.setLore(null);
        demoResurrection.setItemMeta(resurrectionItemMeta);
        return demoResurrection;
    }

    public ItemStack getDemoLuckStone() {
        ItemStack demoLuck = new ItemStack(new LuckStone(1).getItem().getType());
        ItemMeta luckMeta = demoLuck.getItemMeta();

        if (RunesEnchant.is13()) {
            createDemoData("re.demo", luckMeta);
            luckMeta.setDisplayName("Place the luck stone here");
        } else {
            luckMeta.setDisplayName("Place the luck stone here" + demoString);
        }
        luckMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        luckMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        luckMeta.setLore(null);
        demoLuck.setItemMeta(luckMeta);
        return demoLuck;
    }

    public ItemStack getResultantItem() {
        ItemStack resultant = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = resultant.getItemMeta();
        meta.setDisplayName("§a§lClick to Enchant" + HiddenStringUtils.encodeString("btn"));
        resultant.setItemMeta(meta);
        return resultant;
    }

    private void createDemoData(String key, ItemMeta meta) {
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(RunesEnchant.getInstance(), key), PersistentDataType.STRING,
                "true");
    }

}
