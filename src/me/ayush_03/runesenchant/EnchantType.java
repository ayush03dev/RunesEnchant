package me.ayush_03.runesenchant;

public enum EnchantType {

    ARMOR, WEAPON, SWORD, AXE, BOOTS, HELMET, CHESTPLATE, LEGGING, BOW;

    public EnchantType fromString(String str) {
        for (EnchantType type : values()) {
            if (type.toString().equalsIgnoreCase(str)) return type;
        }
        return null;
    }

}
