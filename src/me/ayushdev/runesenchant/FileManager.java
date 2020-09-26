package me.ayushdev.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public FileConfiguration getMessageConfig() {
        File f = new File(dataFolder + File.separator + "messages.yml");

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

    public FileConfiguration getEnchantmentOrbConfig() {
        File f = new File(dataFolder + File.separator + "enchantment-orb.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getSignConfig() {
        File f = new File(dataFolder + File.separator + "rune-sign.yml");
        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getEnchanterConfig() {
        File f = new File(dataFolder + File.separator + "enchanter.yml");

        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getGroupConfig(String groupName) {
        File dir = new File(p.getDataFolder() + File.separator + "groups");
        if (!dir.exists()) dir.mkdirs();

        File f = new File(dir + File.separator + groupName.toLowerCase() + ".yml");
        if (!f.exists()) return null;
        return YamlConfiguration.loadConfiguration(f);
    }

    public List<String> listGroupNames() {
        List<String> list = new ArrayList<>();
        File dir = new File(p.getDataFolder() + File.separator + "groups");
        if (!dir.exists()) dir.mkdirs();

        for (File f : dir.listFiles()) {
            list.add(f.getName().replace(".yml", "").toLowerCase());
        }
        return list;
    }
}
