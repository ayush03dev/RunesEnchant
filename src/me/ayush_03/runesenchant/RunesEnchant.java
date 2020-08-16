package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.commands.RunesCommand;
import me.ayush_03.runesenchant.effects.PVPEffects;
import me.ayush_03.runesenchant.gui.EnchanterGUI;
import me.ayush_03.runesenchant.listeners.EnchanterListener;
import me.ayush_03.runesenchant.listeners.RuneApplyListener;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class RunesEnchant extends JavaPlugin implements Listener {

    private static RunesEnchant instance;
    public static RunesEnchant getInstance() {
        return instance;
    }
    private static int version;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RuneApplyListener(), this);
        getServer().getPluginManager().registerEvents(new PVPEffects(), this);
        getServer().getPluginManager().registerEvents(new EnchanterListener(), this);
        getCommand("runes").setExecutor(new RunesCommand());
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        RunesEnchant.version = Integer.parseInt(version.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));

        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);

        saveResource("enchantments" + File.separator + "aegis.yml", false);
        saveResource("runes.yml", false);
        saveResource("protection-charm.yml", false);
        saveResource("resurrection-stone.yml", false);
        saveResource("enchanter.yml", false);
        saveResource("luck-stone.yml", false);


        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        ItemStack i = p.getInventory().getItemInMainHand();

        if (e.getMessage().equalsIgnoreCase("pc")) {
            ProtectionCharm pc = new ProtectionCharm(1);
            p.getInventory().addItem(pc.createItem());
            return;
        }

        if (e.getMessage().equalsIgnoreCase("enchanter")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    p.openInventory(new EnchanterGUI().createEnchanterGUI(p));
                }
            }.runTaskLater(this, 1);
        }

        if (e.getMessage().equalsIgnoreCase("rs")) {
            p.getInventory().addItem(new ResurrectionStone().getItem());
        }

        if (e.getMessage().equalsIgnoreCase("ls")) {
            p.getInventory().addItem(new LuckStone(1).getItem());

        }

        if (e.getMessage().equalsIgnoreCase("test")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            p.sendMessage(meta.getDisplayName());
            p.sendMessage(HiddenStringUtils.extractHiddenString(meta.getDisplayName()));
            System.out.println(meta.getDisplayName());
        }

//        ItemStack i = p.getInventory().getItemInMainHand();
//        ApplicableItem ai = new ApplicableItem(i);
//        ai.addEnchantment(CustomEnchant.AEGIS, 1);
//        ai.setLevel(CustomEnchant.AEGIS, 3);
//        p.sendMessage("a");
    }

    public static boolean is13() {
        return version >= 13;
    }
}
