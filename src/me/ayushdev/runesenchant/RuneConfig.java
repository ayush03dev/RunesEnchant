package me.ayushdev.runesenchant;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RuneConfig {

    private final FileConfiguration fc;
    private final ConfigurationSection section;

    public RuneConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getRuneConfig();
        this.section = fc.getConfigurationSection(ce.toString().toLowerCase());
    }

    public List<String> getLore() {
        if (section == null) {
            return fc.getStringList("default.lore");
        }
        return fc.getStringList(section.getName() + ".lore");
    }

    public String getDisplayName() {
        if (section == null) {
            return fc.getString("default.display-name");
        }
        return fc.getString(section.getName() + ".display-name");
    }

}
