package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

public class RuneConfig {

    private FileConfiguration fc;

    private RuneConfig() {
        this.fc = FileManager.getInstance().getRuneConfig();
    }

    private static RuneConfig instance = new RuneConfig();

    public static RuneConfig getInstance() {
        return instance;
    }


}
