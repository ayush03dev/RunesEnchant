package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.commands.RunesCommand;
import me.ayush_03.runesenchant.effects.PVPEffects;
import me.ayush_03.runesenchant.listeners.RuneApplyListener;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
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
        getServer().getPluginManager().registerEvents(new PVPEffects(), this);
        getCommand("runes").setExecutor(new RunesCommand());

        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);

        saveResource("enchantments" + File.separator + "aegis.yml", false);
        saveResource("runes.yml", false);
        saveResource("protection-charm.yml", false);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        ProtectionCharm pc = new ProtectionCharm(1);
        p.getInventory().addItem(pc.createItem());

//        ItemStack i = p.getInventory().getItemInMainHand();
//        ApplicableItem ai = new ApplicableItem(i);
//        ai.addEnchantment(CustomEnchant.AEGIS, 1);
//        ai.setLevel(CustomEnchant.AEGIS, 3);
//        p.sendMessage("a");
    }
}
