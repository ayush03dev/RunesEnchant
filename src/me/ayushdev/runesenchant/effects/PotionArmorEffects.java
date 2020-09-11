package me.ayushdev.runesenchant.effects;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PotionArmorEffects extends EnchantmentEffect implements Listener {

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

            if (enchants.containsKey(CustomEnchant.STRENGTH)) {
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }

            if (enchants.containsKey(CustomEnchant.SWIMMER)) {
                p.removePotionEffect(PotionEffectType.WATER_BREATHING);
            }

            if (enchants.containsKey(CustomEnchant.PLATEMAIL)) {
                p.removePotionEffect(PotionEffectType.SLOW);
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }

            if (enchants.containsKey(CustomEnchant.LUMOS)) {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        }

        if (e.getNewArmorPiece() != null) {
            ApplicableItem newArmor = new ApplicableItem(e.getNewArmorPiece());
            Map<CustomEnchant, Integer> enchants = newArmor.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.SPEED) && CustomEnchant.SPEED.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.SPEED);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.SPEED, level, "potion-level") - 1));
            }

            if (enchants.containsKey(CustomEnchant.JUMP) && CustomEnchant.JUMP.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.JUMP);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.JUMP, level, "potion-level") - 1));
            }

            if (enchants.containsKey(CustomEnchant.STRENGTH) && CustomEnchant.STRENGTH.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.STRENGTH);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.STRENGTH, level, "potion-level") - 1));
            }

            if (enchants.containsKey(CustomEnchant.SWIMMER) && CustomEnchant.SWIMMER.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.SWIMMER);
                p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.SWIMMER, level, "potion-level") - 1));
            }

            if (enchants.containsKey(CustomEnchant.LUMOS) && CustomEnchant.LUMOS.getConfig().isEnabled()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            }

            if (enchants.containsKey(CustomEnchant.PLATEMAIL) && CustomEnchant.PLATEMAIL.getConfig().isEnabled()) {
                int level = enchants.get(CustomEnchant.PLATEMAIL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.PLATEMAIL, level, "potion-level.slowness") - 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE,
                        (int) getValue(CustomEnchant.PLATEMAIL, level, "potion-level.resistance") - 1));
            }
        }
    }
}
