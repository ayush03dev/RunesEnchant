package me.ayushdev.runesenchant;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchanterResultant {

    ItemStack item;
    ItemMeta meta;
    FileConfiguration config;

    public EnchanterResultant(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.config = FileManager.getInstance().getEnchanterConfig();
    }

    public void setReadyMessages(Rune rune, LuckStone ls, int netLevel, EnchanterItemMessage... messages) {
        List<String> list = config.getStringList("enchant-button.lore");

        int net = rune.getSuccessRate();
        if (ls != null) {
            net += ls.getIncrease();
        }
        if (net > 100) {
            net = 100;
        }

        List<String> lore = new ArrayList<>();
        for (String str : list) {

            if (str.contains("%luck-applied-message%") && ls == null) continue;

            if (Arrays.asList(messages).contains(EnchanterItemMessage.PROTECTED)) {
                str = str.replace("%check-protection-message%", EnchanterItemMessage.PROTECTED.getMessage());
            } else {
                str = str.replace("%check-protection-message%", EnchanterItemMessage.NOT_PROTECTED.getMessage());

            }

            str = str.replace("%enchantment%", rune.getEnchantment().getDisplayName());
            str = str.replace("%level%", netLevel + "");

            if (ls != null) {
                str = str.replace("%luck-applied-message%", EnchanterItemMessage.LUCK_APPLIED.getMessage()
                        .replace("%level%", ls.getLevel() + "").replace("%increase%", ls.getIncrease() + ""));

                if (str.contains("<br>")) {
                    str = str.replace("<br>", "");
                    str = ChatColor.translateAlternateColorCodes('&', str);
                    lore.add(str);
                    lore.add("");
                    continue;
                }
            }
            str =  str.replace("%success-rate-message%",EnchanterItemMessage.SUCCESS_RATE.getMessage().replace(
                    "%success-rate%", net + ""));
            str = str.replace("%destroy-rate-message%", EnchanterItemMessage.DESTROY_RATE.getMessage().
                    replace("%destroy-rate%", rune.getDestroyRate() + ""));

            str = ChatColor.translateAlternateColorCodes('&', str);

            lore.add(str);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void setErrorMessage(EnchanterItemMessage errorMessage) {
        List<String> list = config.getStringList("error-button.lore");
        List<String> lore = new ArrayList<>();

        list.forEach(str -> {
            str = str.replace("%error%", errorMessage.getMessage());
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore.add(str);
        });
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
