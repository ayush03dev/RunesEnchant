package me.ayush_03.runesenchant.effects;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.EnchantmentEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class ArmorEffects extends EnchantmentEffect implements Listener {

    // SPEED, JUMP

    @EventHandler
    public void onEquip(ArmorEquipEvent e) {
        Player p = e.getPlayer();

        if (e.getOldArmorPiece() != null) {
            ApplicableItem old = new ApplicableItem(e.getOldArmorPiece());
            Map<CustomEnchant, Integer> enchants = old.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }

            if (enchants.containsKey(CustomEnchant.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        }

        if (e.getNewArmorPiece() != null) {
            ApplicableItem newArmor = new ApplicableItem(e.getNewArmorPiece());
            Map<CustomEnchant, Integer> enchants = newArmor.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.SPEED) && CustomEnchant.SPEED.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.SPEED);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,
                        getValue(CustomEnchant.SPEED, level, "potion-level") - 1));
            }

            if (enchants.containsKey(CustomEnchant.JUMP) && CustomEnchant.JUMP.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.JUMP);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE,
                        getValue(CustomEnchant.JUMP, level, "potion-level") - 1));
            }

        }

    }

}
