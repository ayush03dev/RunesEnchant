package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.EnchantmentEffect;
import me.ayushdev.runesenchant.RunesEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ArrowEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            ItemStack bow = e.getBow();
            if (bow == null) return;
            ApplicableItem item = new ApplicableItem(bow);
            if (item.hasEnchantments()) {
                e.getProjectile().setMetadata("re.enchants",
                        new FixedMetadataValue(RunesEnchant.getInstance(), item.getAllCustomEnchantments()));
                e.getProjectile().setMetadata("re.shooter", new FixedMetadataValue(RunesEnchant.getInstance(),
                        e.getEntity().getName()));

                e.getEntity().getWorld().spawnArrow(e.getEntity().getLocation().clone().add(0,2.5,0), rotateVector(e.getProjectile().getVelocity(),  0.1), e.getForce() * 2, 0f);
                e.getEntity().getWorld().spawnArrow(e.getEntity().getLocation().clone().add(0,2.5,0), rotateVector(e.getProjectile().getVelocity(), -0.1), e.getForce() * 2,0f);

            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            if (a.hasMetadata("re.enchants")) {
                System.out.println(a.getMetadata("re.enchants").get(0).value());
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
