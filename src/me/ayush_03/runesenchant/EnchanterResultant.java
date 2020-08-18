package me.ayush_03.runesenchant;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchanterResultant {

    ItemStack item;
    ItemMeta meta;

    public EnchanterResultant(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public void setMessages(EnchanterItemMessage... messages) {
        List<String> lore = new ArrayList<>();

        for (EnchanterItemMessage msg : messages) {
            lore.add(msg.toString());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }


    public void setMessages(Rune rune, int netEnchantmentLevel, LuckStone ls, EnchanterItemMessage... messages) {
        List<String> lore = new ArrayList<>();

        for (EnchanterItemMessage msg : messages) {
            String message = msg.toString();

            if (msg == EnchanterItemMessage.RESULT_ENCHANTMENT) {
                if (rune == null) continue;
                message = message.replace("%enchantment%", rune.getEnchantment().getDisplayName(netEnchantmentLevel));
                lore.add(message);
                lore.add("§7§m------------------");
                lore.add("");
                continue;
            }

            if (msg == EnchanterItemMessage.LUCK_APPLIED) {
                if (ls == null) continue;
                message= message.replace("%level%", ls.getLevel() + "");
                message = message.replace("%increase%", ls.getIncrease() + "");
            } else if (msg == EnchanterItemMessage.SUCCESS_RATE) {
                int net = rune.getSuccessRate();
                if (ls != null) {
                   net += ls.getIncrease();
                }
                if (net > 100) {
                    net = 100;
                }
                message = message.replace("%success%", net + "");
            } else if (msg == EnchanterItemMessage.DESTROY_RATE) {
                message = message.replace("%destroy%", rune.getDestroyRate() + "");
            }

            lore.add(message);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
