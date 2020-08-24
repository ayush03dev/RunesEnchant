package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.RunesEnchant;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class KillingCurse implements Listener {

    int task;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Bukkit.getScheduler().cancelTask(task);

        Location loc = e.getHitEntity() == null ? e.getHitBlock().getLocation() :
                e.getHitEntity().getLocation();

        for (Entity en : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
            if (en instanceof LivingEntity) {
                for (int i = 0; i < 10; i++) {
                    en.getWorld().strikeLightningEffect(en.getLocation());
                }
                ((LivingEntity) en).setHealth(0);
            }
        }

//        if (e.getHitEntity() != null) {
//            if (e.getHitEntity() instanceof LivingEntity) {
//                LivingEntity le = (LivingEntity) e.getHitEntity();
//                le.getWorld().strikeLightningEffect(le.getLocation());
//                le.setHealth(0);
//            }
//        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Player p = e.getPlayer();
            if (p.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
//                Snowball pro = (Snowball) p.launchProjectile(Snowball.class);
                WitherSkull pro = (WitherSkull) p.launchProjectile(WitherSkull.class);
                p.sendTitle("", ChatColor.RED + "AVADA KEDAVRA", 1, 20, 20);

                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(RunesEnchant.getInstance(),
                        new Runnable() {
                            @Override
                            public void run() {
//                                pro.getWorld().playEffect(pro.getLocation(),
//                                        Effect.BLAZE_SHOOT, 1);
                                pro.getWorld().playEffect(pro.getLocation(),
                                        Effect.END_GATEWAY_SPAWN, 0);
                                pro.getWorld().playEffect(pro.getLocation(),
                                        Effect.MOBSPAWNER_FLAMES, 1);
                            }
                        }, 4, 0);
            }
        }
    }

//    @EventHandler
//    public void toggle(PlayerInteractEvent event){
//        final Player player = event.getPlayer();
//        if (event.getAction() == Action.LEFT_CLICK_AIR){
//            new BukkitRunnable(){
//                double t = 0;
//
//                public void run(){
//                    t = t + 0.5;
//                    Location loc = player.getLocation();
//                    Vector direction = loc.getDirection().normalize();
//                    double x = direction.getX() * t;
//                    double y = direction.getY() * t + 1.5;
//                    double z = direction.getZ() * t;
//                    loc.add(x,y,z);
//                    ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, loc);
//                    loc.subtract(x,y,z);
//
//                    if (t > 30){
//                       cancel();
//                    }
//                }
//            }.runTaskTimer(RunesEnchant.getInstance(), 0, 1);
//        }
//    }
}
