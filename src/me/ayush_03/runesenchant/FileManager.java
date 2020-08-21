package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class FileManager {

    private Plugin p;
    private File dataFolder;

    private static final FileManager instance = new FileManager();

    public static FileManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.p = p;
        File dir = p.getDataFolder();
        if (!dir.exists()) dir.mkdir();
        this.dataFolder = dir;
    }

    public FileConfiguration getEnchantmentConfig(CustomEnchant ce) {
        File dir = new File(p.getDataFolder() + File.separator + "enchantments");
        if (!dir.exists()) dir.mkdirs();

        File f = new File(dir + File.separator + ce.toString().toLowerCase() + ".yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getRuneConfig() {
        File f = new File(dataFolder + File.separator + "runes.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getProtectionCharmConfig() {
        File f = new File(dataFolder + File.separator + "protection-charm.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getResurrectionStoneConfig() {
        File f = new File(dataFolder + File.separator + "resurrection-stone.yml");
        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getLuckStoneConfig() {
        File f = new File(dataFolder + File.separator + "luck-stone.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getEnchanterConfig() {
        File f = new File(dataFolder + File.separator + "enchanter.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getGroupConfig(String groupName) {
        File f = new File(dataFolder + File.separator + groupName.toLowerCase() + ".yml");
        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }


}
