package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class FileManager {

    private Plugin p;

    private static FileManager instance = new FileManager();

    public static FileManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.p = p;
    }

    public FileConfiguration getEnchantmentConfig(CustomEnchant ce) {
        File dir = new File(p.getDataFolder() + File.separator + "enchantments");
        if (!dir.exists()) dir.mkdirs();

        File f = new File(dir + File.separator + ce.toString().toLowerCase() + ".yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getRuneConfig() {
        File dir = p.getDataFolder();

        if (!dir.exists()) dir.mkdir();
        File f = new File(dir + File.separator + "runes.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getProtectionCharmConfig() {
        File dir = p.getDataFolder();

        if (!dir.exists()) dir.mkdir();
        File f = new File(dir + File.separator + "protection-charm.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getResurrectionStoneConfig() {
        File dir = p.getDataFolder();

        if (!dir.exists()) dir.mkdir();
        File f = new File(dir + File.separator + "resurrection-stone.yml");
        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getLuckStoneConfig() {
        File dir = p.getDataFolder();

        if (!dir.exists()) dir.mkdir();
        File f = new File(dir + File.separator + "luck-stone.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getEnchanterConfig() {
        File dir = p.getDataFolder();

        if (!dir.exists()) dir.mkdir();
        File f = new File(dir + File.separator + "enchanter.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }


}
