package me.ayushdev.runesenchant;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rune {

    CustomEnchant ce;
    int level, successRate, destroyRate;
    ItemStack runeItem;

    public Rune(CustomEnchant ce, int level, int successRate, int destroyRate) {
        this.ce = ce;
        this.level = level;
        this.successRate = successRate;
        this.destroyRate = destroyRate;
        this.runeItem = createItem();
    }

    public Rune(CustomEnchant ce, int level) {
        this.ce = ce;
        this.level = level;

        RuneConfig cfg = new RuneConfig(ce);
        this.successRate = cfg.getSuccessRate();
        this.destroyRate = cfg.getDestroyRate();

        Random rand = new Random();
        if (successRate == -1) {
            this.successRate = rand.nextInt(100)+1;
        }

        if (destroyRate == -1) {
            this.destroyRate = rand.nextInt(100)+1;
        }

        this.runeItem = createItem();
    }

    public Rune(ItemStack item) {
        if (isRune(item)) {

            String[] args;

            if (RunesEnchant.is13()) {
                ItemMeta meta = item.getItemMeta();
                RunesEnchant instance = RunesEnchant.getInstance();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                args = data.get(new NamespacedKey(instance, "runesenchant.data"), PersistentDataType.STRING).split(":");
            } else {
                args = HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName()).split(":");
            }
            this.ce = CustomEnchant.fromString(args[0].replace("ce-", ""));
            this.level = Integer.parseInt(args[1]);
            this.successRate = Integer.parseInt(args[2]);
            this.destroyRate = Integer.parseInt(args[3]);
            this.runeItem = createItem();
        }
    }

    @Getter
    public CustomEnchant getEnchantment() {
        return ce;
    }

    @Getter
    public int getLevel() {
        return level;
    }

    @Getter
    public int getSuccessRate() {
        return successRate;
    }

    @Getter
    public int getDestroyRate() {
        return destroyRate;
    }

    @Setter
    public void setLevel(int level) {
        this.level = level;
    }

    @Setter
    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    @Setter
    public void setDestroyRate(int destroyRate) {
        this.destroyRate = destroyRate;
    }

    public ItemStack getItem() {
        return runeItem;
    }

    private ItemStack createItem() {
//        ItemStack rune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        RuneConfig cfg = new RuneConfig(ce);
        ItemStack rune = RuneUtils.getInstance().buildItemStack(cfg.getItemId());
        ItemMeta meta = rune.getItemMeta();

        String displayName = cfg.getDisplayName();
        displayName = replace(displayName, "%enchantment%", ce.getDisplayName());
        displayName = replace(displayName, "%level%", level + "");
        displayName = toColor(displayName);

        RunesEnchant re = RunesEnchant.getInstance();
        String hidden = "ce-" + ce.toString() + ':' + level + ':' + successRate + ':' + destroyRate;

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(re, "runesenchant.data"), PersistentDataType.STRING, hidden);
            meta.setDisplayName(displayName);
        } else {
            meta.setDisplayName(displayName + HiddenStringUtils.encodeString(hidden));
        }

        List<String> list = new ArrayList<>();

       cfg.getLore().forEach(line -> {
            line = replace(line, "%enchant%", ChatColor.stripColor(ce.getLoreDisplay(level)));
            line = replace(line, "%level%", level + "");
            line = line.replace("%success%", successRate + "");
            line = line.replace("%destroy%", destroyRate + "");
            line = line.replace("%type%", WordUtils.capitalize(ce.getType().toString().toLowerCase().replace("_", " ")));
            line = toColor(line);
            list.add(line);
        });
        meta.setLore(list);

        rune.setItemMeta(meta);
        return rune;

    }

    public static boolean isRune(ItemStack item) {
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {

                if (RunesEnchant.is13()) {
                    PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                    return data.has(new NamespacedKey(RunesEnchant.getInstance(), "runesenchant.data"), PersistentDataType.STRING);
                }

                if (HiddenStringUtils.hasHiddenString(item.getItemMeta().getDisplayName())
                && HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName()).contains("ce-")) return true;
            }
        }
        return false;
    }

    private String replace(String original, String what, String with) {
        return original.replace(what, with);
    }

    private String toColor(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
