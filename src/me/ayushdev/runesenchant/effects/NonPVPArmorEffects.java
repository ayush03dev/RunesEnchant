package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class NonPVPArmorEffects extends EnchantmentEffect implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            for (ItemStack armor : getArmor(p)) {
                ApplicableItem item = new ApplicableItem(armor);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.DODGE)) {
                    CustomEnchant ce = CustomEnchant.DODGE;
                    if (proc(ce, enchants.get(ce)))
                        e.setDamage(0);
                }

                if ((e.getCause() == DamageCause.FIRE)
                        || (e.getCause() == DamageCause.FIRE_TICK)) {
                    if (enchants.containsKey(CustomEnchant.PYROMANIAC)) {
                        CustomEnchant ce = CustomEnchant.PYROMANIAC;
                        int level = enchants.get(ce);
                        if (proc(ce, level)) {
                            e.setDamage(0);
                            p.setHealth(p.getMaxHealth());
                        }
                    }
                }

                if (e.getCause() == DamageCause.FALL) {
                    if (enchants.containsKey(CustomEnchant.FEATHERFALL)) {
                        CustomEnchant ce = CustomEnchant.FEATHERFALL;
                        int level = enchants.get(ce);
                        if (proc(ce, level))
                            e.setDamage(0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getInventory().getHelmet() != null) {
            Player p = e.getPlayer();
            ItemStack helmet = p.getInventory().getHelmet();
            if (ApplicableItem.isSupportedItem(helmet)) {
                ApplicableItem item = new ApplicableItem(helmet);
                if (item.hasEnchantment(CustomEnchant.EYEPATCH)) {
                    if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                }
            }
        }
    }
}
