package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.listeners.RuneApplyListener;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RunesEnchant extends JavaPlugin implements Listener {

    private static RunesEnchant instance = new RunesEnchant();

    public static RunesEnchant getInstane() {
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RuneApplyListener(), this);

        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        List<String> lore = new ArrayList<>();
        lore.add("Testing lore" + HiddenStringUtils.encodeString("Ayush"));
        lore.add(ChatColor.YELLOW + "My man" + ChatColor.AQUA + "hehe" + HiddenStringUtils.encodeString("Rajat|Ankit|Karan|Sarvesh"));
        lore.add(ChatColor.RED + "Normal lore");
        lore.add(HiddenStringUtils.encodeString("I made it!"));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        lore.forEach(s -> {
            if (HiddenStringUtils.hasHiddenString(s)) {
                System.out.println("yes AND " + HiddenStringUtils.extractHiddenString(s));
            } else {
                System.out.println("NO");
            }
        });

    }
}
