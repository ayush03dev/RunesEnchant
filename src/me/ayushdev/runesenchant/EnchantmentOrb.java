package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EnchantmentOrb {

    int slots;
    EnchantmentOrbConfig config;

    public EnchantmentOrb(int slots) {
        this.slots = slots;
        this.config = new EnchantmentOrbConfig(slots);
    }

    public EnchantmentOrb(ItemStack item) {
        if (isEnchantmentOrb(item)) {
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                this.slots = data.get(new NamespacedKey(RunesEnchant.getInstance(), "re.eo"), PersistentDataType.INTEGER);
            } else {
                this.slots = Integer.parseInt(HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName())
                .replaceAll("[^0-9]", ""));
            }
        }
        this.config = new EnchantmentOrbConfig(slots);
    }

    public int getSlots() {
        return slots;
    }

    public EnchantmentOrbConfig getConfig() {
        return config;
    }

    public ItemStack getItem() {
        ItemStack item = RuneUtils.getInstance().buildItemStack(config.getItemId());
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(RunesEnchant.getInstance(), "re.eo");
            data.set(key, PersistentDataType.INTEGER, slots);
            meta.setDisplayName(config.getDisplayName());
        } else {
            meta.setDisplayName(config.getDisplayName() + HiddenStringUtils.encodeString("eo-slots:" + slots));
        }

        if (config.isGlowing()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.LURE, 1, false);
        }

        meta.setLore(config.getItemLore());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isEnchantmentOrb(ItemStack item) {
        if (item == null) return false;
        if (item.hasItemMeta()) {
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(new NamespacedKey(RunesEnchant.getInstance(), "re.eo"), PersistentDataType.INTEGER);
            } else {
                if (item.getItemMeta().hasDisplayName()
                        && HiddenStringUtils.hasHiddenString(item.getItemMeta().getDisplayName())) {
                    if (HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName()).contains("eo-")) return true;
                }
            }
        }
        return false;
    }
}
