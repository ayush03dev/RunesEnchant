package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.*;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class MobDropListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity en = e.getEntity();
        if (!Settings.getInstance().dropsEnabled()) return;

        if (!(en instanceof Player)) {
            EntityType type = en.getType();

            if (type == EntityType.ARMOR_STAND) return;

            FileConfiguration config = RunesEnchant.getInstance().getConfig();
            float chance = (float) config.getDouble("mob-drops.chance");
            if (RuneUtils.getInstance().generateRandomFloat() <= chance) {

                String path = "mob-drops.drop";
                ConfigurationSection section = config.getConfigurationSection(path + "." + type.toString());

                String defaultPath;
                if (section == null) {
                     defaultPath = path + ".default";
                } else {
                    defaultPath = section.getCurrentPath();
                }

                for (String key : config.getConfigurationSection(defaultPath).getKeys(false)) {
                    CustomEnchant ce;
                    int level;

                    if (key.contains(";")) {
                        String[] args = key.split(";");
                        ce = CustomEnchant.fromString(args[0]);
                        level = Integer.parseInt(args[1]);
                    } else {
                        ce = CustomEnchant.fromString(key);
                        level = new Random().nextInt(ce.getMaxLevel())+1;
                    }

                    if (RuneUtils.getInstance().generateRandomFloat() <= config.getDouble(defaultPath
                            + "." + key)) {
                        Rune rune = new Rune(ce, level);
                        e.getDrops().add(rune.getItem());
                    }
                }

            }
        }
    }

}
