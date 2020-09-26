package me.ayushdev.runesenchant;

import com.codingforcookies.armorequip.ArmorListener;
import me.ayushdev.runesenchant.commands.RItemCommand;
import me.ayushdev.runesenchant.commands.RunesCommand;
import me.ayushdev.runesenchant.effects.*;
import me.ayushdev.runesenchant.gui.EnchanterGUI;
import me.ayushdev.runesenchant.listeners.EnchanterListener;
import me.ayushdev.runesenchant.listeners.RuneApplyListener;

import me.ayushdev.runesenchant.listeners.ShopListener;
import me.ayushdev.runesenchant.listeners.SignEvents;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class RunesEnchant extends JavaPlugin implements Listener {

    private static RunesEnchant instance;
    public static RunesEnchant getInstance() {
        return instance;
    }
    private static int version;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RuneApplyListener(), this);
        getServer().getPluginManager().registerEvents(new PVPWeaponEffects(), this);
        getServer().getPluginManager().registerEvents(new EnchanterListener(), this);
        getServer().getPluginManager().registerEvents(new ShopListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        getServer().getPluginManager().registerEvents(new PotionArmorEffects(), this);
        getServer().getPluginManager().registerEvents(new PVPArmorEffects(), this);
        getServer().getPluginManager().registerEvents(new NonPVPArmorEffects(), this);
        getServer().getPluginManager().registerEvents(new ToolEffects(), this);
        getServer().getPluginManager().registerEvents(new PVEWeaponEffects(), this);
        getServer().getPluginManager().registerEvents(new BowEffects(), this);
        getServer().getPluginManager().registerEvents(new PickaxeEffects(), this);
        getServer().getPluginManager().registerEvents(new WandEffects(), this);
        getServer().getPluginManager().registerEvents(new SignEvents(), this);


        getCommand("runes").setExecutor(new RunesCommand());
        getCommand("ritem").setExecutor(new RItemCommand());

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        RunesEnchant.version = Integer.parseInt(version.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));

        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);

//        for (CustomEnchant ce : CustomEnchant.values()) {
//            try {
//                saveResource("enchantments/" + ce.toString().toLowerCase() + ".yml", false);
//            } catch (IllegalArgumentException ex) {}
//        }

        saveDefaultEnchantments();
        saveDefaultTiers();
        saveDefaultTiers();
        saveDefaultConfig();
        saveDefaultFile("runes.yml");
        saveDefaultFile("protection-charm.yml");
        saveDefaultFile("resurrection-stone.yml");
        saveDefaultFile("enchanter.yml");
        saveDefaultFile("luck-stone.yml");
        saveDefaultFile("rune-sign.yml");
        saveDefaultFile("messages.yml");
        saveDefaultFile("enchantment-orb.yml");
        saveDefaultFile("shop.yml");
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item != null) {
            if (Rune.isRune(item)) {
                Rune rune = new Rune(item);
                p.sendMessage("Enchantment: " + rune.getEnchantment().getDisplayName());
                p.sendMessage("Level: " + rune.getLevel());
                p.sendMessage("Success Rate: " + rune.getSuccessRate() + '%');
                p.sendMessage("Destroy Rate: " + rune.getSuccessRate() + '%');
                p.sendMessage("Description: " + rune.getEnchantment().getDescription());
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        ItemStack i = p.getInventory().getItemInMainHand();

        if (e.getMessage().equalsIgnoreCase("pc")) {
            ProtectionCharm pc = new ProtectionCharm(1);
            p.getInventory().addItem(pc.createItem());
            return;
        }

        if (e.getMessage().equalsIgnoreCase("enchanter")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    p.openInventory(new EnchanterGUI().createEnchanterGUI(p));
                }
            }.runTaskLater(this, 1);
        }

        if (e.getMessage().equalsIgnoreCase("rs")) {
            p.getInventory().addItem(new ResurrectionStone().getItem());
        }

        if (e.getMessage().equalsIgnoreCase("ls")) {
            p.getInventory().addItem(new LuckStone(1).getItem());

        }

        if (e.getMessage().equalsIgnoreCase("eo")) {
            p.getInventory().addItem(new EnchantmentOrb(2).getItem());

        }

        if (e.getMessage().equalsIgnoreCase("test")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            p.sendMessage(meta.getDisplayName());
            p.sendMessage(HiddenStringUtils.extractHiddenString(meta.getDisplayName()));
            System.out.println(meta.getDisplayName());
        }

//        ItemStack i = p.getInventory().getItemInMainHand();
//        ApplicableItem ai = new ApplicableItem(i);
//        ai.addEnchantment(CustomEnchant.AEGIS, 1);
//        ai.setLevel(CustomEnchant.AEGIS, 3);
//        p.sendMessage("a");
    }

    public static boolean is13() {
        return version >= 13;
    }

    private void saveDefaultEnchantments() {
        for (CustomEnchantReference ce : CustomEnchantReference.values()) {
            String name = ce.toString().toLowerCase();
            File f = new File(getDataFolder() + File.separator + "enchantments" + File.separator +  name + ".yml");
            if (!f.exists()) {
                try {
                    saveResource("enchantments/" + name + ".yml", false);
                } catch (IllegalArgumentException e){}
            }
        }
    }

    private void saveDefaultTiers() {
        File f = new File(getDataFolder() + File.separator + "groups" + File.separator + "boots.yml");
        if (!f.exists()) {
            saveResource("groups/boots.yml", false);
        }
    }

    public void saveDefaultFile(String name) {
        File f = new File(getDataFolder() + File.separator + name);
        if (!f.exists()) {
            saveResource(name, false);
        }
    }
}
