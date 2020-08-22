package me.ayush_03.runesenchant.effects;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class NonPVPArmorEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            for (ItemStack armor : getArmor(p)) {
                ApplicableItem item = new ApplicableItem(armor);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.DODGE)) {
                    CustomEnchant ce = CustomEnchant.DODGE;
                    if (proc(ce, enchants.get(ce))) {
                        e.setDamage(0);
                    }
                }
            }
        }
    }
}
