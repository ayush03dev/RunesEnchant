package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class PVPWeaponEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void weaponListener(EntityDamageByEntityEvent e) {

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
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.POISON, potionDuration * 20, potionLevel - 1));
                }
            }

            if (enchants.containsKey(CustomEnchant.CORRUPTION)) {
                CustomEnchant ce = CustomEnchant.CORRUPTION;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, potionDuration * 20, potionLevel - 1));
                }
            }

            if (enchants.containsKey(CustomEnchant.TURMOIL)) {
                CustomEnchant ce = CustomEnchant.TURMOIL;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, potionDuration * 20, potionLevel - 1));
                }
            }

            if (enchants.containsKey(CustomEnchant.HEX)) {
                CustomEnchant ce = CustomEnchant.HEX;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, potionDuration * 20, potionLevel - 1));
                }
            }

            if (enchants.containsKey(CustomEnchant.CURSE)) {
                CustomEnchant ce = CustomEnchant.CURSE;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, potionDuration * 20, potionLevel - 1));
                }
            }

            if (enchants.containsKey(CustomEnchant.HOLY_SMITE)) {
                CustomEnchant ce = CustomEnchant.HOLY_SMITE;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    List<String> list = (List<String>) get(ce, level, "remove-potions");
                    en.getWorld().strikeLightningEffect(en.getLocation());
                    list.forEach(str -> {
                        PotionEffectType type = PotionEffectType.getByName(str);
                        if (type != null) {
                            en.removePotionEffect(type);
                        }
                    });
                }
            }

            if (enchants.containsKey(CustomEnchant.PURGE)) {
                CustomEnchant ce = CustomEnchant.PURGE;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    List<String> list = (List<String>) get(ce, level, "remove-potions");
                    en.getWorld().strikeLightning(en.getLocation());
                    list.forEach(str -> {
                        PotionEffectType type = PotionEffectType.getByName(str);
                        if (type != null) {
                            en.removePotionEffect(type);
                        }
                    });
                }
            }

            if (enchants.containsKey(CustomEnchant.BATTLECRY)) {
                CustomEnchant ce = CustomEnchant.BATTLECRY;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    List<String> list = (List<String>) get(ce, level, "remove-potions");
                    en.getWorld().strikeLightning(en.getLocation());
                    list.forEach(str -> {
                        PotionEffectType type = PotionEffectType.getByName(str);
                        if (type != null) {
                            damager.removePotionEffect(type);
                        }
                    });
                }
            }

            if (enchants.containsKey(CustomEnchant.EXECUTE)) {
                CustomEnchant ce = CustomEnchant.EXECUTE;
                int level = enchants.get(ce);
                if (damager.isSneaking()) {
                    if (proc(ce, level)) {
                        float multiplier = getValue(ce, level, "damage-multiplier");
                        e.setDamage(e.getDamage() * multiplier);
                    }
                }
            }

            if (enchants.containsKey(CustomEnchant.CRUSHING)) {
                CustomEnchant ce = CustomEnchant.CRUSHING;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    float multiplier = getValue(ce, level, "damage-multiplier");
                    e.setDamage(e.getDamage() * multiplier);
                }
            }
        }
    }
}