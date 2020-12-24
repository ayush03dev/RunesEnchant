package me.ayushdev.runesenchant.gui;

import me.ayushdev.runesenchant.*;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsGUIHolder;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsMenuHolder;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsSearchGUIHolder;
import me.ayushdev.runesenchant.inventoryholders.GUIHolder;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentsGUI {

    int pages;

    public EnchantmentsGUI(){};

    public EnchantmentsGUI(EnchantmentGroup group) {
        List<EnchantmentData> list = group.getEnchantments();
        int size = list.size();
        int perPage = 28;
        if (size % perPage == 0) {
            pages = size/perPage;
        } else {
            pages = size/perPage+1;
        }
    }

    public boolean pageExists(int page) {
        return page > 0 && page <= pages;
    }

    public Inventory openMenu(Player p) {
        int total = 0;
        List<EnchantmentGroup> list = new ArrayList<>();
        for (EnchantmentGroup group : EnchantmentGroup.getAllGroups()) {
            if (group.listInGUI())  {
                list.add(group);
                total++;
            }
        }
        int rows;
        if (total % 9 == 0) {
            rows = total/9;
        } else {
            rows = (total/9)+1;
        }
        Inventory inv = Bukkit.createInventory(new EnchantmentsMenuHolder(p), rows*9, "Enchantments");

        for (int i = 0; i < total; i++) {
            inv.setItem(i, list.get(i).getGUIDisplayItem());
        }

        p.openInventory(inv);
        return inv;
    }

    public Inventory openGroupGUI(Player p, EnchantmentGroup group, int page) {
        List<EnchantmentData> list = group.getEnchantments();
        Inventory inv = Bukkit.createInventory(new EnchantmentsGUIHolder(p, group, page), 54, "Enchantments");

        Material mat;
        try {
            mat = Material.WHITE_STAINED_GLASS_PANE;
        } catch (NoSuchFieldError e) {
            mat = Material.matchMaterial("STAINED_GLASS_PANE");
        }

        ItemStack glass = new ItemStack(mat);
        ItemMeta gMeta = glass.getItemMeta();
        gMeta.setDisplayName(ChatColor.BLACK + "");
        glass.setItemMeta(gMeta);
        for (int i = 0; i < 54; i++) {

            if (i >= 0 && i <= 8 || i >= 45 && i <= 53 || (i+1)%9 == 0 || i%9==0) {
                inv.setItem(i, glass);
            }
        }
        int size = list.size();
        int perPage = 28;
        int pages;
        if (size % perPage == 0) {
            pages = size/perPage;
        } else {
            pages = size/perPage+1;
        }

        int startIndex = perPage * (page-1);
        int lastIndex = (perPage*page)-1;

        if (page <= pages && page > 0) {
            // 10 11 12 13 14 15 16
            int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

            int slotIndex = 0;
            for (int i = startIndex; i <= lastIndex; i++) {
                if (i >= list.size()) break;
                EnchantmentData data = list.get(i);
                int slot = slots[slotIndex];
                CustomEnchant ce = data.getCustomEnchant();
                if (!ce.isEnabled()) continue;
                RuneConfig cfg = new RuneConfig(ce);

                ItemStack item = RuneUtils.getInstance().buildItemStack(cfg.getItemId());
                ItemMeta meta = item.getItemMeta();

                FileConfiguration config = RunesEnchant.getInstance().getConfig();
                String name = config.getString("enchantment-list-gui.enchantment-item.display-name");
                name = ChatColor.translateAlternateColorCodes('&', name);
                name = name.replace("%enchantment%", ce.getDisplayName());

                meta.setDisplayName(name);

                List<String> loreList = config.getStringList("enchantment-list-gui.enchantment-item.lore");
                List<String> l = new ArrayList<>();
                loreList.forEach(s -> {
                    boolean desc = false;

                    if (s.contains("%description%")) {
                        desc = true;
                    }

                    s = s.replace("%description%", ce.getDescription());
                    s = s.replace("%max-level%", ce.getMaxLevel() + "");
                    s = s.replace("%type%", WordUtils.capitalize(ce.getType().toString().toLowerCase()));
                    s = ChatColor.translateAlternateColorCodes('&', s);

                    if (desc) {
                        String[] arr = ChatPaginator.wordWrap(s, 30);
                        for (String str : arr) {
                            l.add(str);
                        }
                    } else {
                        l.add(s);
                    }
                });

                meta.setLore(l);
                item.setItemMeta(meta);

                inv.setItem(slot,item);
                slotIndex++;
            }
        }
        // [] [/1] [] [] [.] [] [] [/] []
        inv.setItem(46, makeItem(Material.ARROW, "Previous Page"));
        inv.setItem(49, makeItem(Material.PAPER, "Search"));
        inv.setItem(52, makeItem(Material.ARROW, "Next Page"));
        p.openInventory(inv);
        return inv;
        //0 27
        // 28 55
        // 56 83

        // 0 27
    }

    public Inventory openGroupGUI(Player p, List<CustomEnchant> list, int page) {
        Inventory inv = Bukkit.createInventory(new EnchantmentsSearchGUIHolder(p, list, page), 54, "Enchantments");
        Material mat;
        try {
            mat = Material.WHITE_STAINED_GLASS_PANE;
        } catch (NoSuchFieldError e) {
            mat = Material.matchMaterial("STAINED_GLASS_PANE");
        }

        ItemStack glass = new ItemStack(mat);
        ItemMeta gMeta = glass.getItemMeta();
        gMeta.setDisplayName(ChatColor.BLACK + "");
        glass.setItemMeta(gMeta);

        for (int i = 0; i < 54; i++) {

            if (i >= 0 && i <= 8 || i >= 45 && i <= 53 || (i+1)%9 == 0 || i%9==0) {

                inv.setItem(i, glass);
            }
        }
        int size = list.size();
        int perPage = 28;
        int pages;
        if (size % perPage == 0) {
            pages = size/perPage;
        } else {
            pages = size/perPage+1;
        }

        int startIndex = perPage * (page-1);
        int lastIndex = (perPage*page)-1;

        if (page <= pages && page > 0) {
            // 10 11 12 13 14 15 16
            int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

            int slotIndex = 0;
            for (int i = startIndex; i <= lastIndex; i++) {
                if (i >= list.size()) break;
                CustomEnchant ce = list.get(i);
                int slot = slots[slotIndex];
                RuneConfig cfg = new RuneConfig(ce);

                ItemStack item = RuneUtils.getInstance().buildItemStack(cfg.getItemId());
                ItemMeta meta = item.getItemMeta();

                FileConfiguration config = RunesEnchant.getInstance().getConfig();
                String name = config.getString("enchantment-list-gui.enchantment-item.display-name");
                name = ChatColor.translateAlternateColorCodes('&', name);
                name = name.replace("%enchantment%", ce.getDisplayName());

                meta.setDisplayName(name);

                List<String> loreList = config.getStringList("enchantment-list-gui.enchantment-item.lore");
                List<String> l = new ArrayList<>();
                loreList.forEach(s -> {
                    boolean desc = false;

                    if (s.contains("%description%")) {
                        desc = true;
                    }

                    s = s.replace("%description%", ce.getDescription());
                    s = s.replace("%max-level%", ce.getMaxLevel() + "");
                    s = s.replace("%type%", WordUtils.capitalize(ce.getType().toString().toLowerCase()));
                    s = ChatColor.translateAlternateColorCodes('&', s);

                    if (desc) {
                        String[] arr = ChatPaginator.wordWrap(s, 30);
                        for (String str : arr) {
                            l.add(str);
                        }
                    } else {
                        l.add(s);
                    }
                });

                meta.setLore(l);
                item.setItemMeta(meta);

                inv.setItem(slot,item);
                slotIndex++;
            }
        }
        // [] [/1] [] [] [.] [] [] [/] []
        inv.setItem(46, makeItem(Material.ARROW, "Previous Page"));
        inv.setItem(52, makeItem(Material.ARROW, "Next Page"));
        p.openInventory(inv);
        return inv;
        //0 27
        // 28 55
        // 56 83

        // 0 27
    }

    private ItemStack makeItem(Material mat, String displayName) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }
}
