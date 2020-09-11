package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantmentGroup {

    String name;
    FileConfiguration fc;

    public EnchantmentGroup(String name) {
        this.name = name;
        this.fc = FileManager.getInstance().getGroupConfig(name);
    }

    public static boolean exists(String name) {
        return FileManager.getInstance().getGroupConfig(name.toLowerCase()) != null;
    }

    public static List<String> getAllGroupNames() {
        return FileManager.getInstance().listGroupNames();
    }

    public static List<EnchantmentGroup> getAllGroups() {
        List<EnchantmentGroup> list = new ArrayList<>();
        FileManager.getInstance().listGroupNames().forEach(s -> {
            list.add(new EnchantmentGroup(s));
        });

        return list;
    }

    public String getDescription() {
        try {
            return fc.getString("description");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.out.println("Description of the group: " + name + " is not set!");
            return null;
        }
    }

    public String getDisplayName() {
        try {
            return ChatColor.translateAlternateColorCodes('&', fc.getString("shop-display.display-name"));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.out.println("Display Name of the group: " + name + " is not set!");
            return null;

        }
    }

    public Rune getRandomRune() {
        Random random = new Random();
        List<EnchantmentData> list = getEnchantments();

        EnchantmentData data = list.get(random.nextInt(list.size()));
        CustomEnchant ce = data.getCustomEnchant();
        int level;
        if (!data.allLevels()) {
            level = data.getLevel();
        } else {
            level = random.nextInt(ce.getMaxLevel())+1;
        }

        return new Rune(ce, level);
    }

    public String getName() {
        return name;
    }

//    public List<CustomEnchant> getEnchantments() {
//        List<String> list = fc.getStringList("enchantments");
//        List<CustomEnchant> enchants = new ArrayList<>();
//
//        if (!list.isEmpty()) {
//            list.forEach(string -> {
//                CustomEnchant ce = CustomEnchant.fromString(string);
//                if (ce == null) throw new IllegalArgumentException(string + " is an invalid enchantment id!");
//                enchants.add(CustomEnchant.fromString(string));
//            });
//        }
//        return enchants;
//    }

    public List<EnchantmentData> getEnchantments() {
        List<String> list = fc.getStringList("enchantments");
        List<EnchantmentData> enchants = new ArrayList<>();

        if (!list.isEmpty()) {
            list.forEach(string ->
                enchants.add(new EnchantmentData(string)));
        }
        return enchants;
    }

    public int getPrice() {
        return fc.getInt("price");
    }

    public List<String> getLore() {
        return fc.getStringList("shop-display.lore");
    }

    public String getDisplayItemId() {
        return fc.getString("shop-display.item-id");
    }

    public ItemStack getDisplayItem() {
        ItemStack item = RuneUtils.getInstance().buildItemStack(getDisplayItemId());
        ItemMeta meta = item.getItemMeta();

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(RunesEnchant.getInstance(),
                    "re.group"), PersistentDataType.STRING, name.toLowerCase());
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getDisplayName()));

        } else {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getDisplayName())
            + HiddenStringUtils.encodeString("re.group." + name.toLowerCase()));
        }

        List<String> lore = getLore();
        List<String> list = new ArrayList<>();

        lore.forEach(s -> {
            s = s.replace("%description%", getDescription());
            s = s.replace("%price%", getPrice() + "");
            s = ChatColor.translateAlternateColorCodes('&', s);
            list.add(s);
        });

        meta.setLore(list);
        item.setItemMeta(meta);

        return item;
    }
}
