package me.ayushdev.runesenchant;

public enum CustomEnchantReference {

    AEGIS(EnchantType.AXE), ASSASSIN(EnchantType.SWORD), BEHEAD(EnchantType.SWORD), HEX(EnchantType.SWORD),
    JUMP(EnchantType.BOOTS), SPEED(EnchantType.BOOTS), ZEUS(EnchantType.BOW), LUMOS(EnchantType.HELMET),
    DEMONIC_AURA(EnchantType.ARMOR), DODGE(EnchantType.ARMOR), BLESSED(EnchantType.ARMOR), ARROW_RAIN(EnchantType.BOW),
    CURSE(EnchantType.WEAPON), CRUSHING(EnchantType.WEAPON), EXECUTE(EnchantType.WEAPON),
    STEALTH(EnchantType.ARMOR), PLATEMAIL(EnchantType.CHESTPLATE), PURGE(EnchantType.WEAPON), DIVINE(EnchantType.WEAPON),
    ENTANGLE(EnchantType.BOW), PYROMANIAC(EnchantType.ARMOR), FLAME_CLOAK(EnchantType.ARMOR),BATTLECRY(EnchantType.AXE),
    CORRUPTION(EnchantType.AXE), TURMOIL(EnchantType.AXE), SPIKED(EnchantType.ARMOR), HOLY_SMITE(EnchantType.AXE),
    RIFTSLAYER(EnchantType.SWORD), PETRIFY(EnchantType.AXE), EYEPATCH(EnchantType.HELMET), PLUNDER(EnchantType.WEAPON),
    MISCHIEF(EnchantType.WEAPON), SHADOWSTEP(EnchantType.ARMOR), DEMON_SIPHON(EnchantType.SWORD),
    LUMBERJACK(EnchantType.AXE), MOLTEN(EnchantType.CHESTPLATE), STRENGTH(EnchantType.CHESTPLATE), HASTE(EnchantType.PICKAXE),
    SWIMMER(EnchantType.HELMET), FEATHERFALL(EnchantType.BOOTS), REVERSAL(EnchantType.WEAPON),
    WILD_MARK(EnchantType.BOW), DETONATE(EnchantType.BOW), TRIPLET(EnchantType.BOW), LIFESTEAL(EnchantType.SWORD),
    REINFORCED(EnchantType.CHESTPLATE), REBORN(EnchantType.WEAPON), IMMOLATION(EnchantType.CHESTPLATE),
    PARALYZE(EnchantType.WEAPON), ENDLESS(EnchantType.ANY), SUICIDE(EnchantType.CHESTPLATE),
    NECROMANCER(EnchantType.ARMOR), WOLVES(EnchantType.ARMOR), SOFT_TOUCH(EnchantType.PICKAXE), THOR_HAMMER(EnchantType.SWORD);

    CustomEnchantReference(EnchantType type) {
    }
}
