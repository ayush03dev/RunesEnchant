package me.ayush_03.runesenchant;

public enum CustomEnchant {

    AEGIS(EnchantType.AXE);

    private EnchantType type;
    private CustomEnchant(EnchantType type) {
        this.type = type;
    }

    public EnchantType getType() {
        return type;
    }

    public int getMaxLevel() {
        return 1;
    }


    public static CustomEnchant fromString(String str) {
        for (CustomEnchant ce : values()) {
            if (ce.toString().equalsIgnoreCase(str)) return ce;
        }
        return null;
    }
}
