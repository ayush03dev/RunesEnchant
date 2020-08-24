package me.ayushdev.runesenchant;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentGroup {

    String name;
    FileConfiguration fc;

    public EnchantmentGroup(String name) {
        this.name = name;
        this.fc = FileManager.getInstance().getGroupConfig(name);
    }

    public String getDescription() {
        try {
            return fc.getString("description");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.out.println("Description of the group: " + name + " is not set!");
            return null;
        }
    }

    public String getDisplayName() {
        try {
            return ChatColor.translateAlternateColorCodes('&', fc.getString("display-name"));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.out.println("Display Name of the group: " + name + " is not set!");
            return null;

        }
    }

    public List<CustomEnchant> getEnchantments() {
        List<String> list = fc.getStringList("enchantments");
        List<CustomEnchant> enchants = new ArrayList<>();

        if (!list.isEmpty()) {
            list.forEach(string -> {
                CustomEnchant ce = CustomEnchant.fromString(string);
                if (ce == null) throw new IllegalArgumentException(string + " is an invalid enchantment id!");
                enchants.add(CustomEnchant.fromString(string));
            });
        }
        return enchants;
    }
}
