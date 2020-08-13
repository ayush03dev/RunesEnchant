package me.ayush_03.runesenchant;

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

}
