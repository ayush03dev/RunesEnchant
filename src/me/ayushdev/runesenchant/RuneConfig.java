package me.ayushdev.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RuneConfig {

    private final FileConfiguration fc;
    String section;

    public RuneConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getRuneConfig();
        this.section = ce.toString().toLowerCase();
    }

    public List<String> getLore() {
        if (fc.getStringList(section).isEmpty()) {
            return fc.getStringList("default.lore");
        }

        return fc.getStringList(section + ".lore");
    }

    public String getDisplayName() {
        if (fc.getString(section + ".display-name") == null) {
            return fc.getString("default.display-name");
        }
        return fc.getString(section + ".display-name");
    }

    public int getSuccessRate() {
        int success = fc.getInt(section + ".success-rate");
        if (success == 0) {
            return fc.getInt("default.success-rate");
        }
        return success;
    }

    public int getDestroyRate() {
        int destroy = fc.getInt(section + ".destroy-rate");
        if (destroy == 0) {
            return fc.getInt("default.destroy-rate");
        }
        return destroy;
    }

    public String getItemId() {
        String itemId = fc.getString(section + ".item-id");
        if (itemId == null) {
            return fc.getString("default.item-id");
        }
        return itemId;
    }

}
