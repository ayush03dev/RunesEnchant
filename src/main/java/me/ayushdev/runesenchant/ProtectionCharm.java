package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

// "pc-PROTECTION:" + level + ":" + left

public class ProtectionCharm {

    private int level, left, index;
    private final ItemStack item;
    private ProtectionCharmConfig config;
    private final RunesEnchant instance;

    public ProtectionCharm(int level) {
        this.level = level;
        this.config = new ProtectionCharmConfig(level);
        this.left = config.getStartingDurability(level);
        this.item = createItem();
        this.instance = RunesEnchant.getInstance();
    }

    public ProtectionCharm(ItemStack item) {
        this.item = item;
        this.instance = RunesEnchant.getInstance();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = createNamespace("re.pc");
                if (data.has(key, PersistentDataType.STRING)) {
                    String hidden = data.get(key, PersistentDataType.STRING);
                    String[] args = hidden.split(":");
                    this.level = Integer.parseInt(args[0]);
                    this.left = Integer.parseInt(args[1]);
                    this.index = Integer.parseInt(args[2]);
                    this.config = new ProtectionCharmConfig(level, left);
                }
            } else {

                int counter = -1;
                for (String str : item.getItemMeta().getLore()) {
                    counter++;
                    if (HiddenStringUtils.hasHiddenString(str) &&
                            HiddenStringUtils.extractHiddenString(str).contains("pc-PROTECTION:")) {
                        String hidden = HiddenStringUtils.extractHiddenString(str);
                        hidden = hidden.replace("pc-", "");
                        String[] args = hidden.split(":");

                        this.level = Integer.parseInt(args[1]);
                        this.left = Integer.parseInt(args[2]);
                        this.index = counter;
                        this.config = new ProtectionCharmConfig(level, left);
                        break;

                    }
                }
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLeft(int left) {
        List<String> lore = item.getItemMeta().getLore();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = createNamespace("re.pc");
        if (left == 0) {
            // Remove the protection charm lore...
            lore.remove(index);
            data.remove(key);

        } else {
            this.left = left;
            this.config = new ProtectionCharmConfig(level, left);
            if (RunesEnchant.is13()) {
                lore.set(index, config.getLoreDisplay());
                data.set(key, PersistentDataType.STRING, level + ":" + left + ":" + index);

            } else {
                lore.set(index, config.getLoreDisplay() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void addToLore(List<String> lore) {
        if (RunesEnchant.is13()) {
            lore.add(config.getLoreDisplay());
        } else {
            lore.add(config.getLoreDisplay() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
        }
    }

    public int getLeft() {
        return left;
    }

    public ItemStack createItem() {
        ItemStack item = RuneUtils.getInstance().buildItemStack(config.getItemId());
        ItemMeta meta = item.getItemMeta();

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(createNamespace("re.pc-item"), PersistentDataType.STRING,
                    "pc-PROTECTION:" + level + ":" + left);
            meta.setDisplayName(config.getItemDisplayName());
        } else {
            meta.setDisplayName(config.getItemDisplayName() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
        }

        if (config.isGlowing()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.LURE, 1, false);
        }

        meta.setLore(config.getItemLore());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isProtectionCharmItem(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName()) {
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(createNamespace("re.pc-item"), PersistentDataType.STRING);
            } else {
                String displayName = item.getItemMeta().getDisplayName();
                if (HiddenStringUtils.hasHiddenString(displayName)) {
                    if (HiddenStringUtils.extractHiddenString(displayName).contains("pc-PROTECTION")) return true;
                }
            }
        }
        return false;
    }

    public static String[] getProtectionCharmData(ItemStack protectionCharmItem) {
        if (isProtectionCharmItem(protectionCharmItem)) {
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = protectionCharmItem.getItemMeta().getPersistentDataContainer();
                return data.get(createNamespace("re.pc-item"), PersistentDataType.STRING)
                        .split(":");
            } else {
                return HiddenStringUtils.extractHiddenString(protectionCharmItem.getItemMeta().getDisplayName()).split(":");
            }
        }
        return null;
    }

    private static NamespacedKey createNamespace(String key) {
        return new NamespacedKey(RunesEnchant.getInstance(), key);
    }
}
