package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ToolEffects extends EnchantmentEffect implements Listener {

    @EventHandler
    public void onDamage(PlayerItemDamageEvent e) {
        ApplicableItem item = new ApplicableItem(e.getItem());
        Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

        if (enchants.containsKey(CustomEnchant.ENDLESS)) {
            e.setDamage(0);
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        Inventory inv = p.getInventory();

        ItemStack oldStack = inv.getItem(e.getPreviousSlot());
        ItemStack newStack = inv.getItem(e.getNewSlot());

        if (oldStack == null && newStack == null) return;

        if (!ApplicableItem.isSupportedItem(oldStack) &&
                !ApplicableItem.isSupportedItem(newStack)) return;

        if (oldStack != null) {
            ApplicableItem oldItem = new ApplicableItem(oldStack);
            Map<CustomEnchant, Integer> oldEnchants = oldItem.getAllCustomEnchantments();
            if (oldEnchants.containsKey(CustomEnchant.HASTE)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
        }

        if (newStack != null) {
            ApplicableItem newItem = new ApplicableItem(newStack);
            Map<CustomEnchant, Integer> newEnchants = newItem.getAllCustomEnchantments();
            if (newEnchants.containsKey(CustomEnchant.HASTE)) {
                CustomEnchant ce = CustomEnchant.HASTE;
                int level = newEnchants.get(ce);
                int potionLevel = (int) getValue(ce, level, "potion-level");

                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,
                        Integer.MAX_VALUE, potionLevel-1));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (p.getItemInHand() == null) return;
        ItemStack hand = p.getItemInHand();
        if (!ApplicableItem.isSupportedItem(hand)) return;

        ApplicableItem item = new ApplicableItem(hand);

        Map<CustomEnchant, Integer> enchants = item.getAllCustomEnchantments();

        if (enchants.containsKey(CustomEnchant.LUMBERJACK)) {
            if (e.getBlock().getType().toString().contains("LOG")) {
                List<Material> list = new ArrayList<>();
                list.add(e.getBlock().getType());

                for (Block b : getTree(e.getBlock(), list)) {
                    BlockBreakEvent event = new BlockBreakEvent(b, e.getPlayer());
                    if (!event.isCancelled()) {
                        b.breakNaturally();
                    }
                }
            }
        }
    }

    private Set<Block> getNearbyBlocks(Block start, List<Material> allowedMaterials, HashSet<Block> blocks) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block block = start.getLocation().clone().add(x, y, z).getBlock();
                    if (block != null && !blocks.contains(block) && allowedMaterials.contains(block.getType())) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
                    }
                }
            }
        }
        return blocks;
    }

    public Set<Block> getTree(Block start, List<Material> allowedMaterials) {
        return getNearbyBlocks(start, allowedMaterials, new HashSet<Block>());
    }
}
