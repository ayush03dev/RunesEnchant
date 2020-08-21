package me.ayush_03.runesenchant.effects;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.EnchantmentEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
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

        // TODO: Later check if entity is instance of Player as well...

        if (e.getDamager() instanceof Player) {
            LivingEntity en = (LivingEntity) e.getEntity();
            Player damager = (Player) e.getDamager();

            ApplicableItem item = new ApplicableItem(damager.getItemInHand());
            Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.ASSASSIN)) {
                CustomEnchant ce = CustomEnchant.ASSASSIN;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = getValue(ce, level, "potion-level");
                    int potionDuration = getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.POISON, potionDuration * 20, potionLevel-1));
                }
            }
        }
    }
}