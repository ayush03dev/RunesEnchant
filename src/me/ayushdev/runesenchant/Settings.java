package me.ayushdev.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings {

    private Plugin p;

    private static final Settings instance = new Settings();

    public static Settings getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.p = p;
    }

    private FileConfiguration getConfig() {
        return p.getConfig();
    }

    public String getItemId() {
        return getConfig().getString("rune-item");
    }

    public boolean slotsEnabled() {
        return getConfig().getBoolean("slots-enabled");
    }

    public int getSlots() {
        return getConfig().getInt("slots");
    }

    public String getSlotsDisplay() {
        return getConfig().getString("slot-display");
    }

}
