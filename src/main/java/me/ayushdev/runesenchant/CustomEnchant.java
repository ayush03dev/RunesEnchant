package me.ayushdev.runesenchant;

import org.bukkit.ChatColor;

public enum CustomEnchant {

    AEGIS(EnchantType.AXE), ASSASSIN(EnchantType.SWORD), BEHEAD(EnchantType.SWORD), HEX(EnchantType.SWORD),
    JUMP(EnchantType.BOOTS), SPEED(EnchantType.BOOTS), ZEUS(EnchantType.BOW), LUMOS(EnchantType.HELMET),
    DEMONIC_AURA(EnchantType.ARMOR), DODGE(EnchantType.ARMOR), BLESSED(EnchantType.ARMOR), ARROW_RAIN(EnchantType.BOW),
    CURSE(EnchantType.WEAPON), CRUSHING(EnchantType.WEAPON), EXECUTE(EnchantType.WEAPON),
    STEALTH(EnchantType.ARMOR), PLATEMAIL(EnchantType.CHESTPLATE), PURGE(EnchantType.WEAPON), DIVINE(EnchantType.WEAPON),
    ENTANGLE(EnchantType.BOW), PYROMANIAC(EnchantType.ARMOR), FLAME_CLOAK(EnchantType.ARMOR),BATTLECRY(EnchantType.AXE),
    CORRUPTION(EnchantType.AXE), TURMOIL(EnchantType.AXE), SPIKED(EnchantType.ARMOR), HOLY_SMITE(EnchantType.AXE),
    RIFTSLAYER(EnchantType.SWORD), PETRIFY(EnchantType.AXE), EYEPATCH(EnchantType.HELMET), PLUNDER(EnchantType.WEAPON),
    SHADOWSTEP(EnchantType.ARMOR), DEMON_SIPHON(EnchantType.SWORD),
    LUMBERJACK(EnchantType.AXE), MOLTEN(EnchantType.CHESTPLATE), STRENGTH(EnchantType.CHESTPLATE), HASTE(EnchantType.PICKAXE),
    SWIMMER(EnchantType.HELMET), FEATHERFALL(EnchantType.BOOTS), REVERSAL(EnchantType.WEAPON),
    WILD_MARK(EnchantType.BOW), DETONATE(EnchantType.BOW), TRIPLET(EnchantType.BOW), LIFESTEAL(EnchantType.SWORD),
    REBORN(EnchantType.WEAPON), IMMOLATION(EnchantType.CHESTPLATE),
    PARALYZE(EnchantType.WEAPON), ENDLESS(EnchantType.ANY), SUICIDE(EnchantType.CHESTPLATE),
    NECROMANCER(EnchantType.ARMOR), WOLVES(EnchantType.ARMOR), SOFT_TOUCH(EnchantType.PICKAXE),
    DEATH_HAMMER(EnchantType.BOW), THOR_HAMMER(EnchantType.SWORD), CANON(EnchantType.BOW),
    MONSTER(EnchantType.ARMOR), ALOHOMORA(EnchantType.STICK), PUNCH(EnchantType.WEAPON),
    REPEL(EnchantType.ARMOR), QUAKE(EnchantType.BOOTS), DEVOUR(EnchantType.WEAPON), DECEPTION(EnchantType.CHESTPLATE),
    BIND(EnchantType.ANY), FINISHER(EnchantType.SWORD), SMELT(EnchantType.PICKAXE), DISARM(EnchantType.WEAPON),
    BOMB_SUIT(EnchantType.CHESTPLATE), PHOENIX(EnchantType.ARMOR), BLEED(EnchantType.WEAPON),
    CREEPER(EnchantType.HELMET), ENDER(EnchantType.HELMET);

    private EnchantType type;
    private EnchantmentConfig config;

    CustomEnchant(EnchantType type) {
        this.config = new EnchantmentConfig(this);
        if (config.getConfigFile() != null) {
            EnchantType t = EnchantType.fromString(config.getConfigFile().getString("enchant-type"));
            this.type = t == null ? type : t;
        } else {
            this.type = type;
        }
    }

    public EnchantType getType() {
        return type;
    }

    public int getMaxLevel() {
        return config.getMaxLevel();
    }

    public static CustomEnchant fromString(String str) {
        for (CustomEnchant ce : values()) {
            if (ce.toString().equalsIgnoreCase(str)) return ce;
        }
        return null;
    }

    public EnchantmentConfig getConfig() {
        return config;
    }

    public String getLoreDisplay(int level) {
        String displayName = config.getLoreDisplay();
        displayName = displayName.replace("%level%", level + "");
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        return displayName;
    }

    public String getDisplayName() {
        return config.getDisplayName();
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public String getDescription() {
        return config.getConfigFile().getString("description");
    }

    public int getCost(int level) {
        return getConfig().getCost(level);
    }
}
