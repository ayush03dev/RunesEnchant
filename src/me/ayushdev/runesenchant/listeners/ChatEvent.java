package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentData;
import me.ayushdev.runesenchant.RunesEnchant;
import me.ayushdev.runesenchant.gui.EnchantmentsGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (RunesEnchant.search.containsKey(p)) {
            e.setCancelled(true);
            String message = e.getMessage();
            List<CustomEnchant> list = new ArrayList<>();
            for (EnchantmentData data : RunesEnchant.search.get(p).getEnchantments()) {
                CustomEnchant ce = data.getCustomEnchant();
                if (ce.isEnabled()) {
                    if (ce.getDisplayName().toLowerCase().startsWith(message.toLowerCase()) || ce.getDisplayName().equalsIgnoreCase(message)
                    || ce.toString().toLowerCase().startsWith(message.toLowerCase()) || ce.toString().toLowerCase().equals(message.toLowerCase())) {
                        list.add(ce);
                    }
                }
            }
            RunesEnchant.search.remove(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(RunesEnchant.getInstance(), new Runnable() {
                @Override
                public void run() {
                    new EnchantmentsGUI().openGroupGUI(p, list, 1);
                }
            }, 1);

        }
    }

}
