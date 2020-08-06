package me.ayush_03.runesenchant;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
        ItemStack rune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemMeta meta = rune.getItemMeta();
        RuneConfig cfg = new RuneConfig(ce);

        String displayName = cfg.getDisplayName();
        displayName = replace(displayName, "%enchant%", ce.getDisplayName(level));
        displayName = replace(displayName, "%level%", level + "");
        displayName = toColor(displayName);

        meta.setDisplayName(displayName);

        List<String> list = new ArrayList<>();

        meta.getLore().forEach(line -> {
            line = replace(line, "%enchant%", ce.getDisplayName(level));
            line = replace(line, "%level%", level + "");
            line = toColor(line);
            list.add(line);
        });

        rune.setItemMeta(meta);
        return rune;

    }

    private String replace(String original, String what, String with) {
        return original.replace(what, with);
    }

    private String toColor(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
