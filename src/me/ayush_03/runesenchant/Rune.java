package me.ayush_03.runesenchant;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Rune {

    CustomEnchant ce;
    int level, successRate, destroyRate;

    public Rune(CustomEnchant ce, int level, int successRate, int destroyRate) {
        this.ce = ce;
        this.level = level;
        this.successRate = successRate;
        this.destroyRate = destroyRate;
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

    public void createItem() {
        ItemStack rune = RuneUtils.getInstance().buildItemStack(Settings.getInstance().getItemId());
        ItemMeta meta = rune.getItemMeta();

    }

}
