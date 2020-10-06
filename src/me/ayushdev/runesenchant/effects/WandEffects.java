package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import me.ayushdev.runesenchant.ResurrectionStone;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WandEffects extends EnchantmentEffect implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item != null) {
            ApplicableItem ai = new ApplicableItem(item);
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR
            || e.getAction() == Action.LEFT_CLICK_BLOCK|| e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Map<CustomEnchant, Integer> enchants = ai.getAllCustomEnchantments();
                if (enchants.containsKey(CustomEnchant.ALOHOMORA)) {
                    CustomEnchant ce = CustomEnchant.ALOHOMORA;
                    if (ce.isEnabled()) {
                        int level = enchants.get(ce);

                        Block b = p.getTargetBlock(null, (int) getValue(ce, level, "range"));
                        BlockState state = b.getState();

                        if (state instanceof Container) {
                            Container c = (Container) state;
                            PlayerInteractEvent event = new PlayerInteractEvent(p, e.getAction(), item, b, b.getFace(p.getLocation().getBlock()));
                            if (!event.isCancelled()) {
                                p.openInventory(c.getInventory());
                            }
                        } else {
                            if (state instanceof EnderChest) {
                                PlayerInteractEvent event = new PlayerInteractEvent(p, e.getAction(), item, b, b.getFace(p.getLocation().getBlock()));
                                if (!event.isBlockInHand()) {
                                    p.openInventory(p.getEnderChest());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getHealth() - e.getDamage() <= 0) {
                ItemStack rStone = new ResurrectionStone().getItem();
                if (p.getInventory().contains(rStone)) {
                    e.setCancelled(true);
                    p.getInventory().remove(rStone);
                    p.updateInventory();
                    p.setHealth(p.getMaxHealth());
                    p.setFoodLevel(20);
                    p.sendMessage("§aYou have been resurrected!");
                }
            }
        }
    }
}
