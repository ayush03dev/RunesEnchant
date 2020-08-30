package me.ayushdev.runesenchant.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RuneUtils {

    private static final RuneUtils instance = new RuneUtils();

    public static RuneUtils getInstance() {
        return instance;
    }

    public ItemStack buildItemStack(String itemId) {
        String[] args = itemId.split(":");
        Material mat = Material.matchMaterial(args[0].toUpperCase());

        if (mat == null) return null;
        byte data;
        try {
            data = Byte.parseByte(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("[RunesEnchant] Invalid Rune Item Id in config.yml!");
            return null;
        }

        return new ItemStack(mat, 1, data);
    }

    public int getRandomRate() {
        return new Random().nextInt(100)+1;
    }
}
