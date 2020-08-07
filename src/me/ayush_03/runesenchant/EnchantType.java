package me.ayush_03.runesenchant;

public enum EnchantType {

    ARMOR, WEAPON, SWORD, AXE, BOOTS, HELMET, CHESTPLATE, BOW, HOE, PICKAXE;

    public EnchantType fromString(String str) {
        for (EnchantType type : values()) {
            if (type.toString().equalsIgnoreCase(str)) return type;
        }
        return null;
    }
}
