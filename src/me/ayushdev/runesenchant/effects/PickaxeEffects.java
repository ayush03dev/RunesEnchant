package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.RunesEnchant;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Random;

public class PickaxeEffects implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (p.getItemInHand() != null) {
            ItemStack hand = p.getItemInHand();
            if (ApplicableItem.isSupportedItem(hand)) {
                ApplicableItem item = new ApplicableItem(hand);
                Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

                if (enchants.containsKey(CustomEnchant.SOFT_TOUCH)) {
                    if (e.getBlock().getState() instanceof CreatureSpawner) {
                        if (!e.isCancelled()) {
                            Block b = e.getBlock();
                            CreatureSpawner spawner = (CreatureSpawner) b.getState();
                            ItemStack drop = new ItemStack(spawner.getType());
                            ItemMeta meta = drop.getItemMeta();

                            if (RunesEnchant.is13()) {
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(
                                        new NamespacedKey(RunesEnchant.getInstance(), "re.spawner"),
                                        PersistentDataType.STRING, spawner.getCreatureTypeName().toUpperCase()
                                );
                                meta.setDisplayName(ChatColor.AQUA +
                                        spawner.getCreatureTypeName().toUpperCase() + " Spawner");
                            } else {
                                meta.setDisplayName(ChatColor.AQUA +
                                        spawner.getCreatureTypeName().toUpperCase() + " Spawner"
                                + HiddenStringUtils.encodeString("re.spawner|" + spawner.getCreatureTypeName().toUpperCase()));
                            }

                            drop.setItemMeta(meta);

                            b.getLocation().getWorld().dropItem(b.getLocation(), drop);
                        }
                    }
                }

                if (enchants.containsKey(CustomEnchant.SMELT)) {
                    if (CustomEnchant.SMELT.isEnabled()) {
                        Block b = e.getBlock();
                        int fortune = calculateFortune(p, b.getType());

                        if (b.getType() == Material.IRON_ORE) {
                            e.setDropItems(false);
                            b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.IRON_INGOT, fortune));
                        }

                        if (b.getType() == Material.GOLD_ORE) {
                            e.setDropItems(false);
                            b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.GOLD_INGOT, fortune));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getState() instanceof CreatureSpawner) {
            Block b = e.getBlock();
            if (e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName()) {
                ItemStack hand = e.getItemInHand();
                ItemMeta meta = hand.getItemMeta();

                EntityType et = null;

                if (RunesEnchant.is13()) {
                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(RunesEnchant.getInstance(), "re.spawner");
                    if (data.has(key, PersistentDataType.STRING)) {
                        et = EntityType.valueOf(data.get(key, PersistentDataType.STRING).toUpperCase());
                    }
                } else {
                    if (HiddenStringUtils.hasHiddenString(meta.getDisplayName())) {
                        String data = HiddenStringUtils.extractHiddenString(meta.getDisplayName());
                        if (data.startsWith("re.spawner|")) {
                            data = data.replace("re.spawner|", "");
                            et = EntityType.valueOf(data.toUpperCase());
                        }
                    }
                }
//                String displayName = e.getItemInHand().getItemMeta().getDisplayName();
//                displayName = displayName.replace(" Spawner", "");
//                EntityType type = EntityType.valueOf(ChatColor.stripColor(displayName).toUpperCase());
                if (et == null) return;
                CreatureSpawner s = (CreatureSpawner) b.getState();
                s.setSpawnedType(et);
                s.update();
            }
        }
    }

    public int calculateFortune(Player p, Material block) {
        int fortune = 1;

        if (p.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = new Random().nextInt(p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
            if (fortune <= 0) fortune = 1;
            return (block == Material.LAPIS_ORE ? 4 + new Random().nextInt(5) : 1) * (fortune + 1);
        }

        return fortune;
    }
}
