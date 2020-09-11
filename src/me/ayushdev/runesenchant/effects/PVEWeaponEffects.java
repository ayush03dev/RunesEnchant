package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PVEWeaponEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onPVE(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player) &&
                (!(e.getEntity() instanceof  Player))) {

            if (!(e.getEntity() instanceof LivingEntity)) return;

            LivingEntity en = (LivingEntity) e.getEntity();

            Player damager = (Player) e.getDamager();
            ItemStack hand = damager.getItemInHand();

            if (!ApplicableItem.isSupportedItem(hand)) return;
            ApplicableItem item = new ApplicableItem(hand);
            Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.RIFTSLAYER)) {
                CustomEnchant ce = CustomEnchant.RIFTSLAYER;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    float multiplier = getValue(ce, level, "damage-multiplier");
                    e.setDamage(e.getDamage() * multiplier);
                }
            }

            if (enchants.containsKey(CustomEnchant.DEMON_SIPHON)) {
                CustomEnchant ce = CustomEnchant.DEMON_SIPHON;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    float percent = getValue(ce, level, "health-percent");
                    double hearts = (percent / 100f) * en.getHealth();
                    en.setHealth(en.getHealth() - hearts);
                    damager.setHealth(damager.getHealth() + hearts);
                }
            }
        }

        // Temporary..
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity en = e.getEntity();
        if (en.getKiller() != null) {
            Player killer = en.getKiller();
            if (killer.getItemInHand() != null) {
                if (ApplicableItem.isSupportedItem(killer.getItemInHand())) {
                    ApplicableItem item = new ApplicableItem(killer.getItemInHand());
                    Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                    if (enchants.containsKey(CustomEnchant.PLUNDER)) {
                        CustomEnchant ce = CustomEnchant.PLUNDER;
                        int level = enchants.get(ce);
                        float multiplier = getValue(ce, level, "xp-multiplier");
                        e.setDroppedExp((int) (e.getDroppedExp() * multiplier));
                    }
                }
            }
        }
    }
}
