package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PVPWeaponEffects extends EnchantmentEffect implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void weaponListener(EntityDamageByEntityEvent e) {

        if (e.isCancelled()) return;

        // TODO: Later check if entity is instance of Player as well...
        if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow) {
            if (!(e.getEntity() instanceof Player)) return;
            LivingEntity en = (LivingEntity) e.getEntity();

            Player damager = null;
            Map<CustomEnchant, Integer> enchants = null;

            if (e.getDamager() instanceof Arrow) {
                Arrow a = (Arrow) e.getDamager();
                if (a.hasMetadata("re.enchants")) {
                    damager = Bukkit.getPlayer((String) Objects.requireNonNull(a.getMetadata("re.shooter").get(0).value()));
                    enchants = (Map<CustomEnchant, Integer>) a.getMetadata("re.enchants").get(0).value();
                }
            } else {
                damager = (Player) e.getDamager();
                enchants = new ApplicableItem(damager.getItemInHand()).getAllCustomEnchantments();
            }

            if (enchants == null || damager == null) return;

//
//            ApplicableItem item = new ApplicableItem(damager.getItemInHand());
//            Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

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

            if (enchants.containsKey(CustomEnchant.THOR_HAMMER)) {
                CustomEnchant ce = CustomEnchant.THOR_HAMMER;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    if (((Player) en).getItemInHand().getType() != Material.AIR) {
                        Player pl = (Player) en;
                        ItemStack hand = pl.getItemInHand();
                        ApplicableItem item = new ApplicableItem(hand);
                        Map<CustomEnchant, Integer> secondMap = item.getAllCustomEnchantments();

                        if (!secondMap.containsKey(CustomEnchant.ENDLESS)) {
                            pl.setItemInHand(new ItemStack(Material.AIR));
                            pl.getWorld().playSound(pl.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                        }
                    }
                }
            }

            if (enchants.containsKey(CustomEnchant.DISARM)) {
                CustomEnchant ce = CustomEnchant.DISARM;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    if (((Player) en).getItemInHand().getType() != Material.AIR) {
                        Player pl = (Player) en;
                        ItemStack hand = pl.getItemInHand();
                        pl.getWorld().dropItem(pl.getLocation(), hand);
                    }
                }
            }

            if (enchants.containsKey(CustomEnchant.FINISHER)) {
                CustomEnchant ce = CustomEnchant.FINISHER;
                int level = enchants.get(ce);
                float threshold = getValue(ce, level, "health");
                if (en.getHealth() - e.getDamage() <= threshold) {
                    if (proc(ce, level)) {
                        en.setHealth(0);
                    }
                }
            }

            if (enchants.containsKey(CustomEnchant.PUNCH)) {
                CustomEnchant ce = CustomEnchant.PUNCH;
                int level = enchants.get(ce);

                if (proc(ce, level)) {
                    float velocity = getValue(ce, level, "velocity");
                    en.setVelocity(damager.getLocation().getDirection().multiply(velocity));
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

            if (enchants.containsKey(CustomEnchant.PETRIFY)) {
                CustomEnchant ce = CustomEnchant.PETRIFY;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int potionLevel = (int) getValue(ce, level, "potion-level");
                    int potionDuration = (int) getValue(ce, level, "potion-duration");
                    en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, potionDuration * 20, potionLevel - 1));
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
                    final Player localDamager = damager;
                    list.forEach(str -> {
                        PotionEffectType type = PotionEffectType.getByName(str);
                        if (type != null) {
//                            damager.removePotionEffect(type);
                            localDamager.removePotionEffect(type);
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

            if (enchants.containsKey(CustomEnchant.BLEED)) {
                CustomEnchant ce = CustomEnchant.BLEED;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    int duration = (int) getValue(ce, level, "bleed-duration") * 20;
                    float damage = getValue(ce, level, "bleed-damage");
                    en.sendMessage(ChatColor.DARK_RED + "You are bleeding!");

                    new BukkitRunnable() {
                        int counter = 0;

                        @Override
                        public void run() {
                            if (!en.isValid()) {
                                cancel();
                            }

                            if (counter >= duration) cancel();
                            en.damage(damage);
                            counter++;
                        }
                    }.runTaskTimer(RunesEnchant.getInstance(), 0, 20);

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

            if (enchants.containsKey(CustomEnchant.LIFESTEAL)) {
                CustomEnchant ce = CustomEnchant.LIFESTEAL;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    float percent = getValue(ce, level, "health-percent");
                    double health = en.getHealth();
                    double transfer = (percent / 100f) * health;
                    en.setHealth(en.getHealth() - transfer);
                    damager.setHealth(damager.getHealth() + transfer);
                }
            }

            if (enchants.containsKey(CustomEnchant.DIVINE)) {
                CustomEnchant ce = CustomEnchant.DIVINE;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    float heal = getValue(ce, level, "health", new Placeholder("%damage%", e.getDamage()));
                    damager.setHealth(damager.getHealth() + heal);
                }
            }

            if (enchants.containsKey(CustomEnchant.REVERSAL)) {
                CustomEnchant ce = CustomEnchant.REVERSAL;
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    double damage = e.getDamage();
                    e.setDamage(0);
                    damager.damage(damage);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        List<ItemStack> drops = new ArrayList<>();
        drops.addAll(e.getDrops());

        List<ItemStack> save = new ArrayList<>();

        for (ItemStack item : drops) {
            ApplicableItem ai = new ApplicableItem(item);
            if (ai.hasEnchantment(CustomEnchant.BIND)) {
                if (CustomEnchant.BIND.isEnabled()) {
                    e.getDrops().remove(item);
                    save.add(item);
                }
            }
        }

        RunesEnchant.reDrop.put(p, save);


        if (p.getKiller() == null) return;

        Player killer = p.getKiller();
        ItemStack hand = killer.getItemInHand();

        if (!ApplicableItem.isSupportedItem(hand)) return;
        ApplicableItem item = new ApplicableItem(hand);
        Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

        if (enchants.containsKey(CustomEnchant.BEHEAD)) {
            CustomEnchant ce = CustomEnchant.BEHEAD;
            int level = enchants.get(ce);
            if (proc(ce, level)) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner(p.getName());
                skull.setItemMeta(meta);

                p.getWorld().dropItem(p.getLocation(), skull);
            }
        }

        if (enchants.containsKey(CustomEnchant.REBORN)) {
            CustomEnchant ce = CustomEnchant.BEHEAD;
            if (ce.isEnabled()) {
                int level = enchants.get(ce);
                int potionDuration = (int) getValue(ce, level, "potion-duration") * 20;
                int potionLevel = (int) getValue(ce, level, "potion-level");

                PotionEffect pe1 = new PotionEffect(PotionEffectType.ABSORPTION, potionDuration, potionLevel - 1);
                PotionEffect pe2 = new PotionEffect(PotionEffectType.REGENERATION, potionDuration, potionLevel - 1);

                killer.addPotionEffect(pe1);
                killer.addPotionEffect(pe2);
            }
        }

        if (enchants.containsKey(CustomEnchant.DEVOUR)) {
            CustomEnchant ce = CustomEnchant.DEVOUR;
            if (ce.isEnabled()) {
                int level = enchants.get(ce);
                if (proc(ce, level)) {
                    killer.setHealth(killer.getMaxHealth());
                    killer.setFoodLevel(20);
                }
            }
        }

        if (enchants.containsKey(CustomEnchant.DECEPTION)) {
            CustomEnchant ce = CustomEnchant.DECEPTION;
            if (ce.isEnabled()) {
                int level = enchants.get(ce);
                int potionDuration = (int) getValue(ce, level, "potion-duration") * 20;
                int potionLevel = (int) getValue(ce, level, "potion-level") - 1;
                killer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, potionDuration, potionLevel));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (RunesEnchant.reDrop.containsKey(p)) {
            List<ItemStack> list = RunesEnchant.reDrop.get(p);
            list.forEach(i -> {
                p.getInventory().addItem(i);
            });
        }
        p.updateInventory();
        RunesEnchant.reDrop.remove(p);
    }
}