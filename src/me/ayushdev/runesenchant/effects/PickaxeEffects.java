package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.RunesEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

                            meta.setDisplayName(ChatColor.AQUA +
                                    spawner.getCreatureTypeName().toUpperCase() + " Spawner");
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
                String displayName = e.getItemInHand().getItemMeta().getDisplayName();
                displayName = displayName.replace(" Spawner", "");
                EntityType type = EntityType.valueOf(ChatColor.stripColor(displayName).toUpperCase());
                CreatureSpawner s = (CreatureSpawner) b.getState();
                s.setSpawnedType(type);
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
