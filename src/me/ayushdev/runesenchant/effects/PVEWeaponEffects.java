package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PVEWeaponEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onPVE(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player) &&
                (!(e.getEntity() instanceof  Player))) {

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
        }

        // Temporary..
    }
}
