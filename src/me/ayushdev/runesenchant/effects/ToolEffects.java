package me.ayushdev.runesenchant.effects;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.EnchantmentEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class ToolEffects extends EnchantmentEffect implements Listener {

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
}
