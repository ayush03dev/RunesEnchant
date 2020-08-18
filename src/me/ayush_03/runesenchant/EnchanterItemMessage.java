package me.ayush_03.runesenchant;

public enum EnchanterItemMessage {

    UNCHANGED("Place the Rune and the Applicable item to enchant!"),
    NO_ITEM("Place the item"),
    NO_RUNE("Place the Rune"),
    NOT_APPLICABLE("The item is not applicable with the Rune"),
    NOT_PROTECTED("Enchanting process won't be protected"),
    PROTECTED("Enchanting is protected"),
    MAX_LEVEL("Enchantment already at a max level"),
    LOW_LEVEL("Level lower than the existing level"),
    LUCK_APPLIED("§d§lLuck Stone %level% §7§l(§a§l+%increase%% Success Rate§7§l)"),
    RESULT_ENCHANTMENT("%enchantment%"),
    SUCCESS_RATE("§a§l%success%% Success Rate"),
    DESTROY_RATE("§c§l%destroy%% Destroy Rate");

    private final String message;

    EnchanterItemMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
