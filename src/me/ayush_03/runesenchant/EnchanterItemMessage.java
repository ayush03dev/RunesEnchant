package me.ayush_03.runesenchant;

public enum EnchanterItemMessage {

    UNCHANGED("Place the Rune and the Applicable item to enchant!"),
    NO_ITEM("Place the item"),
    NO_RUNE("Place the Rune"),
    NOT_APPLICABLE("The item is not applicable with the Rune"),
    NOT_PROTECTED("Enchanting process won't be protected"),
    PROTECTED("Enchanting is protected"),
    CHANCES("");

    private final String message;

    EnchanterItemMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
