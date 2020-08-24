package me.ayushdev.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

public enum EnchanterItemMessage {

    DEFAULT("Place the Rune and the Applicable item to enchant!"),
    NO_ITEM("Place the item"),
    NO_RUNE("Place the Rune"),
    NOT_APPLICABLE("The item is not applicable with the Rune"),
    NOT_PROTECTED("Enchanting process won't be protected"),
    PROTECTED("Enchanting is protected"),
    MAX_LEVEL("Enchantment already at a max level"),
    LOW_LEVEL("Level lower than the existing level"),
    LUCK_APPLIED("§d§lLuck Stone %level% §7§l(§a§l+%increase%% Success Rate§7§l)"),
    RESULT_ENCHANTMENT("§a§l%enchantment% §d§l%level%"),
    SUCCESS_RATE("§a§l%success%% Success Rate"),
    DESTROY_RATE("§c§l%destroy%% Destroy Rate");

    // TODO: Add support for Slots...

    private final String message;
    private final FileConfiguration config;

    EnchanterItemMessage(String message) {
        this.message = message;
        this.config = FileManager.getInstance().getEnchanterConfig();
    }

    @Override
    public String toString() {
        return message;
    }

    public String getMessage() {
        return config.getString("lore-messages." + super.toString().replace("_", "-").toLowerCase());
    }
}
