package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PickaxeEffects implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() != null) {
            ItemStack hand = p.getItemInHand();
            if (ApplicableItem.isSupportedItem(hand)) {
                ApplicableItem item = new ApplicableItem(hand);
                if (e.getBlock().getType().toString().contains("SPAWNER")) {
                    if (!e.isCancelled()) {
                        Block b = e.getBlock();
                        CreatureSpawner spawner = (CreatureSpawner) b.getState();
                        ItemStack drop  = new ItemStack(spawner.getType());
                        drop.setData(spawner.getData());

                        b.getLocation().getWorld().dropItem(b.getLocation(), drop);
                    }
                }
            }
        }
    }

}
