package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.*;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SignEvents implements Listener {

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("runesenchant.sign.create")) return;
        String[] lines = e.getLines();
        if (lines[0].equalsIgnoreCase("[RunesEnchant]")) {
            CustomEnchant ce = CustomEnchant.fromString(lines[1]);
            if (ce == null) return;
            int level = 0, xp = 0;
            try {
                level = Integer.parseInt(lines[2]);
                xp = Integer.parseInt(lines[3]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }

            if (level > ce.getMaxLevel()) {
                p.sendMessage(ChatColor.RED + "Error: Level is exceeding the max level of the enchantment.");
                return;
            }

            for (String key : FileManager.getInstance().getSignConfig().getKeys(false)) {
                String value = FileManager.getInstance().getSignConfig().getString(key);
                value = value.replace("%enchantment%", ce.getDisplayName());
                value = value.replace("%level%", level + "");
                value = value.replace("%cost%", xp + "");
                value = ChatColor.translateAlternateColorCodes('&', value);

                key = key.replace("line-", "");
                int line = Integer.parseInt(key);
                e.setLine(line-1, value);
            }

            e.getBlock().setMetadata("re.sign", new FixedMetadataValue(
                    RunesEnchant.getInstance(), ce.toString() + ':' + level + ':' + xp
            ));

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (!p.hasPermission("runesenchant.sign.use")) return;
            Block b = e.getClickedBlock();
            if (b.getState() instanceof Sign) {
                Sign sign = (Sign) b.getState();

                if (sign.hasMetadata("re.sign")) {
                    String data = (String) sign.getMetadata("re.sign").get(0).value();
                    String[] args = data.split(":");
                    CustomEnchant ce = CustomEnchant.fromString(args[0]);
                    int level = Integer.parseInt(args[1]);
                    int cost = Integer.parseInt(args[2]);

                    if (RuneUtils.getInstance().getTotalExperience(p) < cost) {
                        MessageManager.getInstance().sendMessage(p, Message.NO_XP);
                        return;
                    }

                    Rune rune = new Rune(ce, level);
                    RuneUtils utils = RuneUtils.getInstance();
                    utils.setTotalExperience(utils.getTotalExperience(p) - cost, p);

                    MessageManager.getInstance().sendMessage(p,
                            Message.RUNE_RECEIVED, new Placeholder("%enchantment%",
                                    ce.getDisplayName()), new Placeholder("%level%", level));

                    if (p.getInventory().firstEmpty() == -1) {
                        p.getWorld().dropItem(p.getLocation(), rune.getItem());
                        MessageManager.getInstance().sendMessage(p, Message.ITEM_DROPPED);
                    } else {
                        p.getInventory().addItem(rune.getItem());
                    }
                }
            }
        }
    }

}
