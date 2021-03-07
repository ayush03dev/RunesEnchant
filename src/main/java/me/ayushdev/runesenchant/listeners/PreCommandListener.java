package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCommandListener implements Listener {

    @EventHandler
    public void onExecute(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        if (message.startsWith("/")) {
            message = message.substring(1);
            FileConfiguration fc = FileManager.getInstance().getAliasConfig();
            if (fc.isSet(message.toLowerCase())) {
                if (fc.getString(message.toLowerCase()) != null) {
                    e.setCancelled(true);
                    p.performCommand(fc.getString(message.toLowerCase()));
                }
            }
        }
    }
}
