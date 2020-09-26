package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

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
                    if (e.getBlock().getType().toString().contains("SPAWNER")) {
                        if (!e.isCancelled()) {
                            Block b = e.getBlock();
                            CreatureSpawner spawner = (CreatureSpawner) b.getState();
                            ItemStack drop = new ItemStack(spawner.getType());
                            drop.setData(spawner.getData());

                            b.getLocation().getWorld().dropItem(b.getLocation(), drop);
                        }
                    }
                }
            }
        }
    }
}
