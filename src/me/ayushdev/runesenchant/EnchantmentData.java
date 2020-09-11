package me.ayushdev.runesenchant;

public class EnchantmentData {

    CustomEnchant ce;
    int level;

    public EnchantmentData(String data) {
        if (data.contains(":")) {
            String[] args = data.split(":");
            ce = CustomEnchant.fromString(args[0]);
            level = Integer.parseInt(args[1]);

            if (level > ce.getMaxLevel()) {
                throw new IllegalArgumentException(level + " exceeds max level of " + ce.toString() + '!');
            }

        } else {
            ce = CustomEnchant.fromString(data);
            level = 0;
        }

        if (ce == null) throw new IllegalArgumentException(data + " is an invalid enchantment id!");
    }

    public boolean allLevels() {
        return level == 0;
    }

    public CustomEnchant getCustomEnchant() {
        return ce;
    }

    public int getLevel() {
        return level;
    }
}
