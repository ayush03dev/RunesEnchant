package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import me.ayushdev.runesenchant.Placeholder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
                    if (ce.isEnabled()) {
                        if (proc(ce, enchants.get(ce)))
                            e.setDamage(0);
                    }
                }

                if (enchants.containsKey(CustomEnchant.BOMB_SUIT)) {
                    CustomEnchant ce = CustomEnchant.BOMB_SUIT;
                    if (ce.isEnabled()) {
                        if (e.getCause().toString().contains("EXPLOSION")) {
                            e.setDamage(0);
                        }
                    }
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

                    if (enchants.containsKey(CustomEnchant.PHOENIX)) {
                        if (CustomEnchant.PHOENIX.isEnabled()) {
                            e.setDamage(0);
                        }
                    }
                }

                if (e.getCause() == DamageCause.FALL) {
                    if (enchants.containsKey(CustomEnchant.FEATHERFALL)) {
                        CustomEnchant ce = CustomEnchant.FEATHERFALL;
                        if (ce.isEnabled()) {
                            int level = enchants.get(ce);
                            if (proc(ce, level))
                                e.setDamage(0);
                        }
                    }

                    if (enchants.containsKey(CustomEnchant.QUAKE)) {
                        CustomEnchant ce = CustomEnchant.QUAKE;
                        if (ce.isEnabled()) {
                            int level = enchants.get(ce);
                            if (proc(ce, level)) {
                                int radius = (int) getValue(ce, level, "radius");
                                int damage = (int) getValue(ce, level, "damage", new Placeholder("%damage%", e.getDamage()));

                                for (Entity en : p.getNearbyEntities(radius, radius, radius)) {
                                    if (en instanceof LivingEntity) {
                                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, en, DamageCause.CUSTOM, damage);
                                        if (!event.isCancelled()) {
                                            ((LivingEntity) en).damage(damage);
                                        }
                                    }
                                }
                            }
                        }
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
