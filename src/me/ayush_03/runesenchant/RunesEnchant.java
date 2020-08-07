package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.commands.RunesCommand;
import me.ayush_03.runesenchant.listeners.RuneApplyListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class RunesEnchant extends JavaPlugin implements Listener {

    private static RunesEnchant instance;

    public static RunesEnchant getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RuneApplyListener(), this);
        getCommand("runes").setExecutor(new RunesCommand());

        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);

        saveResource("enchantments" + File.separator + "aegis.yml", false);
        saveResource("runes.yml", false);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }
}
