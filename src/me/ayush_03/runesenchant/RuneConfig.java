package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RuneConfig {

    private FileConfiguration fc;
    private CustomEnchant ce;

    public RuneConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getRuneConfig();
        this.ce = ce;
    }

    public List<String> getLore() {
        return fc.getStringList(ce.toString().toLowerCase() + ".lore");
    }

    public String getDisplayName() {
        return fc.getString(ce.toString().toLowerCase() + ".display-name");
    }

}
