package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import me.ayushdev.runesenchant.Placeholder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PVPArmorEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void armorListener(EntityDamageByEntityEvent e) {
        // TODO: Later check if entity is instance of Player as well...

//        if (e.getDamager() instanceof Player) {
        if (e.getEntity() instanceof Player) {
            LivingEntity en = (LivingEntity) e.getEntity();
            LivingEntity damager = (LivingEntity) e.getDamager();
//            Player damager = (Player) e.getDamager();

            Player p = (Player) en;

            for (ItemStack armor : getArmor(p)) {
                ApplicableItem item = new ApplicableItem(armor);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.DEMONIC_AURA)) {
                    CustomEnchant ce = CustomEnchant.DEMONIC_AURA;
                    int level = enchants.get(ce);
                    if (proc(ce, level)) {
                        int potionLevel = (int) getValue(ce, level, "potion-level");
                        int potionDuration = (int) getValue(ce, level, "potion-duration");
                        en.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, potionDuration * 20, potionLevel -1));
                    }
                }

                if (enchants.containsKey(CustomEnchant.FLAME_CLOAK)) {
                    CustomEnchant ce = CustomEnchant.FLAME_CLOAK;
                    int level = enchants.get(ce);
                    if (proc(ce, level)) {
                        int time = (int) getValue(ce, level, "fire-duration");
                        damager.setFireTicks(time);
                    }
                }

                if (enchants.containsKey(CustomEnchant.SPIKED)) {
                    CustomEnchant ce = CustomEnchant.SPIKED;
                    int level = enchants.get(ce);
                    if (proc(ce, level)) {
                        float damage = getValue(ce, level, "damage", new Placeholder("%damage%",
                                e.getDamage()));
                        damager.damage(damage);
                    }
                }
            }
        }
    }
}
