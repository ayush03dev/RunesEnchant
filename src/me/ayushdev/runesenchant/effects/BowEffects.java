package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import me.ayushdev.runesenchant.RunesEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;

public class BowEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            ItemStack bow = e.getBow();
            if (bow == null) return;
            ApplicableItem item = new ApplicableItem(bow);

            if (item.hasEnchantments()) {
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.TRIPLET)) {
                   Arrow a1 = e.getEntity().getWorld().spawnArrow(e.getEntity().getLocation().clone().add(0,2.5,0), rotateVector(e.getProjectile().getVelocity(),  0.1), e.getForce() * 2, 0f);
                   Arrow a2 =e.getEntity().getWorld().spawnArrow(e.getEntity().getLocation().clone().add(0,2.5,0), rotateVector(e.getProjectile().getVelocity(), -0.1), e.getForce() * 2,0f);

                    a1.setMetadata("re.enchants",
                            new FixedMetadataValue(RunesEnchant.getInstance(), item.getAllCustomEnchantments()));
                    a1.setMetadata("re.shooter", new FixedMetadataValue(RunesEnchant.getInstance(),
                            e.getEntity().getName()));

                    a2.setMetadata("re.enchants",
                            new FixedMetadataValue(RunesEnchant.getInstance(), item.getAllCustomEnchantments()));
                    a2.setMetadata("re.shooter", new FixedMetadataValue(RunesEnchant.getInstance(),
                            e.getEntity().getName()));
                }

                e.getProjectile().setMetadata("re.enchants",
                        new FixedMetadataValue(RunesEnchant.getInstance(), item.getAllCustomEnchantments()));
                e.getProjectile().setMetadata("re.shooter", new FixedMetadataValue(RunesEnchant.getInstance(),
                        e.getEntity().getName()));

            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            if (a.hasMetadata("re.enchants")) {
                if (!(e.getEntity() instanceof Player)) return;
                Player p = (Player) e.getEntity();
                Map<CustomEnchant, Integer> enchants = (Map<CustomEnchant, Integer>) a.getMetadata("re.enchants").get(0).value();

                if (enchants.containsKey(CustomEnchant.WILD_MARK)) {
                    CustomEnchant ce = CustomEnchant.WILD_MARK;
                    int level = enchants.get(ce);
                    float multiplier = getValue(ce, level, "damage-multiplier");
                    e.setDamage(multiplier * e.getDamage());
                }

                if (enchants.containsKey(CustomEnchant.DETONATE)) {
                    CustomEnchant ce = CustomEnchant.DETONATE;
                    int level = enchants.get(ce);
                    if (proc(ce, level)) {
                        e.getEntity().getWorld().createExplosion(
                                e.getEntity().getLocation(), getValue(ce, level, "power"),
                                (boolean) get(ce, level, "break-blocks"));
                    }
                }

                if (enchants.containsKey(CustomEnchant.ENTANGLE)) {
                    CustomEnchant ce = CustomEnchant.ENTANGLE;
                    int level = enchants.get(ce);
                    if (proc(ce, level)) {
                        int potionLevel = (int) getValue(ce, level, "potion-level");
                        int potionDuration = (int) getValue(ce, level, "potion-duration");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, potionDuration, potionLevel-1));
                    }
                }
            }
        }
    }

    public Vector rotateVector(Vector vector, double whatAngle) {
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }
}
