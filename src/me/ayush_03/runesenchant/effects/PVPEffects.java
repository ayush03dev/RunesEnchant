package me.ayush_03.runesenchant.effects;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PVPEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            ApplicableItem item = new ApplicableItem(damager.getInventory().getItemInMainHand());
            Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.ASSASSIN)) {
                CustomEnchant ce = CustomEnchant.ASSASSIN;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = getValue(ce, level, "potion-level");
                    int potionDuration = getValue(ce, level, "potion-duration");

                    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, potionDuration, potionLevel-1));
                }
            }
        }
    }
}