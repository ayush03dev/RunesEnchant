package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PVPArmorEffects extends EnchantmentEffect implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void armorListener(EntityDamageByEntityEvent e) {

        if (e.isCancelled()) return;
        // TODO: Later check if damager is instance of Player as well...

        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                LivingEntity en = (LivingEntity) e.getEntity();
//            LivingEntity damager = (LivingEntity) e.getDamager();
                Player damager = (Player) e.getDamager();
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
                            en.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, potionDuration * 20, potionLevel - 1));
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

                    if (enchants.containsKey(CustomEnchant.NECROMANCER)) {
                        CustomEnchant ce = CustomEnchant.NECROMANCER;
                        int level = enchants.get(ce);
                        if (proc(ce, level)) {
                            int amount = (int) getValue(ce, level, "amount");

                            for (int i = 0; i < amount; i++) {
                                Zombie z = p.getWorld().spawn(p.getLocation(), Zombie.class);
                                z.setMetadata("re.necromancer",
                                        new FixedMetadataValue(RunesEnchant.getInstance(), p.getUniqueId()));
                                z.setBaby(false);
                                z.setTarget(damager);
                            }
                        }
                    }


                    if (enchants.containsKey(CustomEnchant.PARALYZE)) {
                        CustomEnchant ce = CustomEnchant.PARALYZE;
                        int level = enchants.get(ce);

                        if (proc(ce, level)) {
                            int duration = (int) getValue(ce, level, "potion-duration");
                            int potionLevel = (int) getValue(ce, level, "potion-level");
                            en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, potionLevel - 1));
                        }
                    }

                    if (enchants.containsKey(CustomEnchant.AEGIS)) {
                        CustomEnchant ce = CustomEnchant.AEGIS;
                        int level = enchants.get(ce);

                        if (proc(ce, level)) {
                            if (p.isBlocking()) {
                                double damage = e.getDamage();
                                float percent = getValue(ce, level, "damage-percent");
                                double health = (percent / 100d) * damage;
                                p.setHealth(p.getHealth() + health);
                            }
                        }
                    }

                    if (enchants.containsKey(CustomEnchant.REPEL)) {
                        CustomEnchant ce = CustomEnchant.REPEL;
                        int level = enchants.get(ce);

                        if (proc(ce, level)) {
                            float velocity = getValue(ce, level, "velocity");
                            damager.setVelocity(p.getLocation().getDirection().multiply(velocity));
                        }
                    }

                    if (enchants.containsKey(CustomEnchant.WOLVES)) {
                        CustomEnchant ce = CustomEnchant.WOLVES;
                        int level = enchants.get(ce);
                        if (proc(ce, level)) {
                            int amount = (int) getValue(ce, level, "amount");

                            for (int i = 0; i < amount; i++) {
                                Wolf w = p.getWorld().spawn(p.getLocation(), Wolf.class);
                                w.setOwner(p);
                                w.setTarget(damager);
                            }
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

                    if (enchants.containsKey(CustomEnchant.SHADOWSTEP)) {
                        CustomEnchant ce = CustomEnchant.SHADOWSTEP;
                        int level = enchants.get(ce);
                        if (proc(ce, level)) {

                            boolean flag = false;
                            boolean flag2 = false;

                            Location location = null;

                            for (int i = 1; i <= 2; i++) {
                                Vector vector = damager.getLocation().getDirection();
                                vector = vector.multiply(i * -1.0);
                                location = damager.getLocation().add(vector);
                                Block b = location.getBlock();
                                Block up = b.getRelative(BlockFace.UP);

                                if (!(b.getType() == Material.AIR && up.getType() == Material.AIR)) break;
                                if (i == 1) {
                                    flag = true;
                                } else {
                                    flag2 = true;
                                }
                            }

                            if (flag && flag2) {
                                p.teleport(location);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if (e.isCancelled()) return;

        if (e.getEntity() instanceof Monster) {
            if (e.getTarget() instanceof Player) {
                Player p = (Player) e.getTarget();
                for (ItemStack armor : getArmor(p)) {
                    ApplicableItem item = new ApplicableItem(armor);
                    if (item.getAllCustomEnchantments().containsKey(CustomEnchant.MONSTER)) {
                        if (CustomEnchant.MONSTER.isEnabled()) e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) e.getEntity();
            if (z.hasMetadata("re.necromancer")) {
                UUID id = (UUID) z.getMetadata("re.necromancer").get(0).value();
                if (e.getTarget() == null) return;
                if (e.getTarget().getUniqueId().equals(id)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        for (ItemStack armor : getArmor(p)) {
            ApplicableItem item = new ApplicableItem(armor);
            Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

            if (enchants.containsKey(CustomEnchant.STEALTH)) {
                CustomEnchant ce = CustomEnchant.STEALTH;

                int level = enchants.get(ce);
                float range = getValue(ce, level, "radius");

                for (Entity en : p.getNearbyEntities(range, range, range)) {
                    if (en instanceof Player) {
                        Player player = (Player) en;

                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, en, EntityDamageEvent.DamageCause.CUSTOM,
                                new HashMap<>(), new HashMap<>());
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) continue;

                        int potionLevel = (int) getValue(ce, level, "potion-level");
                        int duration = (int) getValue(ce, level, "potion-duration");

                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration
                        * 20, potionLevel-1));
                    }
                }
            }

            if (enchants.containsKey(CustomEnchant.IMMOLATION)) {
                CustomEnchant ce = CustomEnchant.IMMOLATION;
                int level = enchants.get(ce);
                float range = getValue(ce, level, "radius");

                for (Entity en : p.getNearbyEntities(range, range, range)) {
                    if (en instanceof Player) {
                        Player player = (Player) en;

                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, en, EntityDamageEvent.DamageCause.CUSTOM,
                                new HashMap<>(), new HashMap<>());
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) continue;

                        int duration = (int) getValue(ce, level, "fire-duration");
                        player.setFireTicks(duration * 20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        for (ItemStack armor : getArmor(p)) {
            if (armor != null) {
                ApplicableItem item = new ApplicableItem(armor);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.SUICIDE)) {
                    CustomEnchant ce = CustomEnchant.SUICIDE;
                    int level = enchants.get(ce);

                    if (proc(ce, level)) {
                        float power = getValue(ce, level, "explosion-power");
                        boolean setFire = (boolean) get(ce, level, "set-fire");
                        boolean breakBlocks = (boolean) get(ce, level, "break-blocks");
                        p.getWorld().createExplosion(p.getLocation(), power,
                                setFire, breakBlocks);
                    }
                }
            }
        }
    }

}