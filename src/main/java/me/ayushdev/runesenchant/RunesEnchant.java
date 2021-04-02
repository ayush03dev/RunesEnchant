package me.ayushdev.runesenchant;

import com.codingforcookies.armorequip.ArmorListener;
import me.ayushdev.runesenchant.commands.*;
import me.ayushdev.runesenchant.effects.*;
import me.ayushdev.runesenchant.listeners.*;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunesEnchant extends JavaPlugin implements Listener {

    public static Map<Player, List<ItemStack>> reDrop = new HashMap<>();

    private static RunesEnchant instance;
    public static RunesEnchant getInstance() {
        return instance;
    }
    private static int version;
    protected static String userID = "%%__USER__%%";
//    public static List<Player> search = new ArrayList<>();
    public static Map<Player, EnchantmentGroup> search = new HashMap<>();

    public static Economy econ = null;
    public static boolean vault_init = false;


    @Override
    public void onEnable() {
        instance = this;


        if (setupEconomy()) {
            vault_init = true;
//            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling RunesEnchant due to " +
//                    "unavailability of Vault!");
//            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling RunesEnchant due to " +
//                    "unavailability of Vault!");
//            getServer().getPluginManager().disablePlugin(this);
//            return;
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RunesEnchant] Either Vault has not been installed on the server," +
                    " or there is no economy plugin hooked up with Vault.");
        }

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
        getServer().getPluginManager().registerEvents(new TinkererListener(), this);
        getServer().getPluginManager().registerEvents(new MobDropListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentsGUIListener(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new PreCommandListener(), this);


        getCommand("runes").setExecutor(new RunesCommand());
        getCommand("ritem").setExecutor(new RItemCommand());
        getCommand("id").setExecutor(new IDCommand());
        getCommand("tinkerer").setExecutor(new TinkererCommand());
        getCommand("enchanter").setExecutor(new EnchanterCommand());
        getCommand("setslots").setExecutor(new SetSlotsCommand());

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        RunesEnchant.version = Integer.parseInt(version.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));
//        int test = Integer.parseInt("1_16_R3".replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));
//
//        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RunesEnchant] Version Read: " + version);
//        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RunesEnchant] Version Read: " + test);


        Settings.getInstance().setup(this);
        FileManager.getInstance().setup(this);

//        for (CustomEnchant ce : CustomEnchant.values()) {
//            try {
//                saveResource("enchantments/" + ce.toString().toLowerCase() + ".yml", false);
//            } catch (IllegalArgumentException ex) {}
//        }

        saveDefaultEnchantments();
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
        saveDefaultFile("command-aliases.yml");

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
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
                getConfig().getStringList("rune-right-click-message").forEach(str -> {
                    str = ChatColor.translateAlternateColorCodes('&', str);
                    str = str.replace("%enchantment%", rune.getEnchantment().getDisplayName());
                    str = str.replace("%level%", rune.getLevel() + "");
                    str = str.replace("%success%", rune.getSuccessRate() + "");
                    str = str.replace("%destroy%", rune.getDestroyRate() + "");
                    str = str.replace("%description%", rune.getEnchantment().getDescription());
                    p.sendMessage(str);
                });
//                p.sendMessage("Enchantment: " + rune.getEnchantment().getDisplayName());
//                p.sendMessage("Level: " + rune.getLevel());
//                p.sendMessage("Success Rate: " + rune.getSuccessRate() + '%');
//                p.sendMessage("Destroy Rate: " + rune.getDestroyRate() + '%');
//                p.sendMessage("Description: " + rune.getEnchantment().getDescription());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (search.containsKey(p)) {
            search.remove(p);
        }
    }

    public static boolean is13() {
        return version >= 14;
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

        for (EnchantType type : EnchantType.values()) {
            File f = new File(getDataFolder() + File.separator + "groups" + File.separator + type.toString().toLowerCase() + ".yml");
            if (!f.exists()) {
                saveResource("groups/" + type.toString().toLowerCase() + ".yml", false);
            }
        }

//        File f = new File(getDataFolder() + File.separator + "groups" + File.separator + "boots.yml");
//        if (!f.exists()) {
//            saveResource("groups/boots.yml", false);
//        }
    }

    public void saveDefaultFile(String name) {
        File f = new File(getDataFolder() + File.separator + name);
        if (!f.exists()) {
            saveResource(name, false);
        }
    }

//    private void sortEnchantments() {
//        FileConfiguration fc = FileManager.getInstance().getGroupConfig("boots");
//        for (CustomEnchant ce : CustomEnchant.values()) {
//            EnchantType type = ce.getType();
//            List<String> list = fc.getStringList(type.toString());
//            list.add(ce.toString().toLowerCase());
//            fc.set(type.toString(), list);
//        }
//
//        try {
//            fc.save(new File(getDataFolder() + File.separator + "groups" + File.separator + "boots.yml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
