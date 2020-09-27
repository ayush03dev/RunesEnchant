package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class PickaxeEffects implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (p.getItemInHand() != null) {
            ItemStack hand = p.getItemInHand();
            if (ApplicableItem.isSupportedItem(hand)) {
                ApplicableItem item = new ApplicableItem(hand);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();
                if (enchants.containsKey(CustomEnchant.SOFT_TOUCH)) {
                    if (e.getBlock().getState() instanceof CreatureSpawner) {
                        if (!e.isCancelled()) {
                            Block b = e.getBlock();
                            CreatureSpawner spawner = (CreatureSpawner) b.getState();
                            ItemStack drop = new ItemStack(spawner.getType());
                            ItemMeta meta = drop.getItemMeta();
                            meta.setDisplayName(ChatColor.AQUA +
                                    spawner.getCreatureTypeName().toUpperCase() + " Spawner");
                            drop.setItemMeta(meta);

                            b.getLocation().getWorld().dropItem(b.getLocation(), drop);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getState() instanceof CreatureSpawner) {
            Block b = e.getBlock();
            if (e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName()) {
                String displayName = e.getItemInHand().getItemMeta().getDisplayName();
                displayName = displayName.replace(" Spawner", "");
                EntityType type = EntityType.valueOf(ChatColor.stripColor(displayName).toUpperCase());
                CreatureSpawner s = (CreatureSpawner) b.getState();
                s.setSpawnedType(type);
                s.update();
            }
        }
    }
}
