package me.ayush_03.runesenchant.effects;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.EnchantmentEffect;
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
            }
        }
    }

}
