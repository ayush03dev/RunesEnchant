package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.ExpressionResolver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class EnchantmentEffect {

    public float getChance(CustomEnchant ce, int level) {
        EnchantmentConfig config = ce.getConfig();
        ConfigurationSection section = config.getConfigFile().getConfigurationSection("chance");

        if (section == null) return 0;

        float override = (float) config.getConfigFile().getDouble(section.getName() + ".level_" + level);

        if (override == 0) {
            String expression = config.getConfigFile().getString(section.getName() + ".expression");
            if (expression == null) return 0;
            try {
                return ExpressionResolver.getInstance().solve(expression.replace("%level%", level + ""));
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Invalid chance expression!");
                return 0;
            }
        } else {
            return override;
        }
    }

    public float getValue(CustomEnchant ce, int level, String path) {
        FileConfiguration config = ce.getConfig().getConfigFile();
        if (config == null) return 0;
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return 0;

        float value = (float) config.getDouble(path + ".level_" + level);
        if (value == 0) {

            String expression = config.getString(path + ".expression");
            if (expression == null) return 0;

            try {
                return ExpressionResolver.getInstance().solve(expression.replace("%level%", level + ""));
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Invalid expression!");
                return 0;
            }
        } else {
            return value;
        }
    }

    public float getValue(CustomEnchant ce, int level, String path, Placeholder... placeholders) {

        FileConfiguration config = ce.getConfig().getConfigFile();
        if (config == null) return 0;
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return 0;

        float value = (float) config.getDouble(path + ".level_" + level);
        if (value == 0) {

            String expression = config.getString(path + ".expression");
            if (expression == null) return 0;

            if (placeholders.length != 0) {
                for (Placeholder holder : placeholders) {
                    expression = expression.replace(holder.getName(), holder.getData() + "");
                }
            }

            try {
                return ExpressionResolver.getInstance().solve(expression.replace("%level%", level + ""));
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Invalid expression!");
                return 0;
            }
        } else {
            return value;
        }
    }

    public Object get(CustomEnchant ce, int level, String path) {
        FileConfiguration config = ce.getConfig().getConfigFile();
        if (config == null) return null;
        return config.get(path);
    }

    public Object get(CustomEnchant ce, int level, String path, ConfigDataType type) {
        FileConfiguration config = ce.getConfig().getConfigFile();
        if (config == null) return null;

        if (type == ConfigDataType.INTEGER) {
            return config.getInt(path);
        }

        if (type == ConfigDataType.FLOAT) {
            return config.getDouble(path);
        }

        if (type == ConfigDataType.LIST) {
            return config.getString(path);
        }

        if (type == ConfigDataType.BOOLEAN) {
            return config.getBoolean(path);
        }

        return null;
    }

//    public int generateRandom() {
//        return new Random().nextInt(100)+1;
//    }

    public static double generateRandomFloat() {
        double r =  ThreadLocalRandom.current().nextDouble(0, 100);
        double scale = Math.pow(10, 2);
        r = Math.round(r * scale) / scale;

        return r;
    }

    public ItemStack[] getArmor(Player p) {
        List<ItemStack> list = new ArrayList<>(4);
        for (ItemStack armorContent : p.getInventory().getArmorContents()) {
            if (armorContent != null) {
                list.add(armorContent);
            }
        }

        return list.toArray(new ItemStack[0]);
    }



//    public boolean proc(CustomEnchant ce, int level) {
//        if (generateRandom() <= getChance(ce, level)) return true;
//        return false;
//    }
public boolean proc(CustomEnchant ce, int level) {
    if (!ce.isEnabled()) return false;
    if (generateRandomFloat() <= getChance(ce, level)) return true;
    return false;
}
}
