package me.ayush_03.runesenchant;

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
            return name.contains("_HELMET") || name.contains("_CHESTPLATE")
                    || name.contains("_LEGGINGS") || name.contains("_BOOTS");
        } else if (this == WEAPON) {
            return name.contains("_SWORD") || name.contains("_sAXE");
        } else {
            return name.contains("_" + toString());
        }
    }
}
