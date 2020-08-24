package me.ayushdev.runesenchant;

import org.bukkit.inventory.ItemStack;

public enum EnchantType {

    ARMOR, WEAPON, SWORD, AXE, BOOTS, HELMET, CHESTPLATE, BOW, HOE, PICKAXE;

    public static EnchantType fromString(String str) {
        for (EnchantType type : values()) {
            if (type.toString().equalsIgnoreCase(str)) return type;
        }
        return null;
    }

    public boolean isApplicableItem(ItemStack item) {
        String name = item.getType().toString();

        if (this == ARMOR) {
            return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                    || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
        } else if (this == WEAPON) {
            return name.endsWith("_SWORD") || name.endsWith("_AXE");
        } else {
            return name.contains("_" + toString());
        }
    }
}
