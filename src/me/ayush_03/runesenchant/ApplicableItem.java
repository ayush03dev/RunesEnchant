package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ApplicableItem {

    private ItemStack item;
    private int slots;
    private int lineIndex;
    private boolean initialized;
    private final RunesEnchant instance;

    public ApplicableItem(ItemStack item) {
        this.item = item;
        this.instance = RunesEnchant.getInstance();

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            this.slots = getAvailableSlots();

            List<String> lore = item.getItemMeta().getLore();

            if (Settings.getInstance().slotsEnabled()) {
                if (RunesEnchant.is13()) {
                    PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                    if (data.has(new NamespacedKey(instance, "re.slots"), PersistentDataType.STRING)) {
                        String hidden = data.get(new NamespacedKey(instance, "re.slots"), PersistentDataType.STRING);
                        String[] args = hidden.split(":");
                        this.slots = Integer.parseInt(args[0]);
                        this.lineIndex = Integer.parseInt(args[1]);
                        this.initialized = true;
                    }
                } else {

                    for (int i = 0; i < lore.size(); i++) {

                        String str = lore.get(i);

                        if (HiddenStringUtils.hasHiddenString(str) &&
                                HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                            this.lineIndex = i;
                            this.initialized = true;
                            break;
                        }
                    }
                    if (!initialized) {
                        this.lineIndex = lore.size();
                    }
                }
            }

        } else {
            this.initialized = false;
            this.slots = Settings.getInstance().slotsEnabled() ? Settings.getInstance().getSlots() : 1;
        }
    }

    public ItemStack getItemStack() {
        return item;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;

        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }

        String line = Settings.getInstance().getSlotsDisplay().replace("%slots%", slots + "");
        line = ChatColor.translateAlternateColorCodes('&', line);

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (!initialized) {
                data.set(new NamespacedKey(RunesEnchant.getInstance(), "re.slots"), PersistentDataType.STRING,
                        slots + ":" + lore.size());
                lore.add(line);
            } else {
                lore.set(lineIndex, line);
                data.set(new NamespacedKey(RunesEnchant.getInstance(), "re.slots"), PersistentDataType.STRING,
                        slots + ":" + lineIndex);
            }
        } else {

            line = line + HiddenStringUtils.encodeString("slots:" + slots);

            if (!initialized) {
                lore.add(line);
            } else {
                lore.set(lineIndex, line);
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public boolean hasEnchantment(CustomEnchant ce) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(new NamespacedKey(instance, "re.ce." + ce.toString()),
                        PersistentDataType.STRING);
            }

            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("ce-", "");
                    String[] args = hidden.split(":");
                    return ce == CustomEnchant.fromString(args[0]);
                }
            }
        }
        return false;
    }

    /**
     * @return Returns a map of all enabled custom enchants
     */

    public Map<CustomEnchant, Integer> getAllCustomEnchantments() {
        Map<CustomEnchant, Integer> map = new HashMap<>();

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                data.getKeys().forEach(namespace -> {
                    String key = namespace.getKey();
                    if (key.contains("re.ce.")){
                        CustomEnchant ce = CustomEnchant.fromString(key.replace("re.ce.", ""));
                        int level = Integer.parseInt(data.get(namespace, PersistentDataType.STRING).split(":")[0]);
                        map.put(ce, level);
                    }
                });
            } else {

                for (String str : item.getItemMeta().getLore()) {
                    if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                        String hidden = HiddenStringUtils.extractHiddenString(str);
                        hidden = hidden.replace("ce-", "");
                        String[] args = hidden.split(":");
                        CustomEnchant ce = CustomEnchant.fromString(args[0]);

                        if (ce.getConfig().isEnabled()) {
                            int level = Integer.parseInt(args[1]);
                            if (level > ce.getMaxLevel()) {
                                map.put(ce, ce.getMaxLevel());
                            } else {
                                map.put(ce, Integer.parseInt(args[1]));
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    public Response verifyAddEnchantment(CustomEnchant ce, int level) {
        if (!hasEnchantment(ce)) {

            ItemMeta meta = item.getItemMeta();

            if (meta.hasLore()) {
                if (initialized) {
                    if (Settings.getInstance().slotsEnabled()) {
                        if (slots == 0) return Response.ERROR_NO_SLOT;
                    }
                }
            }
        } else {
            return Response.ERROR_EXSTS;
        }
        return Response.AVAILABLE;
    }

    public void addEnchantment(CustomEnchant ce, int level) {
            List<String> lore;
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                lore = meta.getLore();
            } else {
                lore = new ArrayList<>();
            }

            if (RunesEnchant.is13()) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                data.set(new NamespacedKey(RunesEnchant.getInstance(), "re.ce." + ce.toString()), PersistentDataType.STRING,
                        level + ":" + lore.size());

                lore.add(ce.getLoreDisplay(level));
            } else {
                lore.add(ce.getLoreDisplay(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
    }

    public void setLevel(CustomEnchant ce, int level) {
        if (level <= ce.getMaxLevel()) {
            if (hasEnchantment(ce)) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (RunesEnchant.is13()) {
                    int index = getEnchantmnentIndex(ce);
                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    data.set(new NamespacedKey(instance, "re.ce." + ce.toString()), PersistentDataType.STRING, level + ":" + index);
                    lore.set(index, ce.getLoreDisplay(level));

                } else {
                    lore.set(getEnchantmnentIndex(ce), ce.getLoreDisplay(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }

    public int getLevel(CustomEnchant ce) {
        if (RunesEnchant.is13()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            String hidden = data.get(new NamespacedKey(instance, "re.ce." + ce.toString()), PersistentDataType.STRING);
            if (hidden != null) {
                return Integer.parseInt(hidden.split(":")[0]);
            }
        } else {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                for (String str : item.getItemMeta().getLore()) {
                    if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                        String hidden = HiddenStringUtils.extractHiddenString(str);
                        hidden = hidden.replace("ce-", "");
                        String[] args = hidden.split(":");
                        if (ce == CustomEnchant.fromString(args[0])) return Integer.parseInt(args[1]);
                    }
                }
            }
        }
        return 0;
    }

    private int getAvailableSlots() {
        if (!Settings.getInstance().slotsEnabled()) return 1;

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            if (RunesEnchant.is13()) {
                NamespacedKey nk = new NamespacedKey(instance, "re.slots");
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                if (data.has(nk, PersistentDataType.STRING)) {
                    return Integer.parseInt(data.get(nk, PersistentDataType.STRING).split(":")[0]);
                }
            } else {

                for (String str : item.getItemMeta().getLore()) {
                    if (HiddenStringUtils.hasHiddenString(str) &&
                            HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                        String hidden = HiddenStringUtils.extractHiddenString(str);
                        hidden = hidden.replace("slots:", "");
                        return Integer.parseInt(hidden);
                    }
                }
            }

        }
        return Settings.getInstance().getSlots();
    }

    private int getEnchantmnentIndex(CustomEnchant ce) {
        if (RunesEnchant.is13()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            if (data.has(new NamespacedKey(instance, "re.ce." + ce.toString()), PersistentDataType.STRING)) {
                return Integer.parseInt(data.get(new NamespacedKey(instance, "re.ce." + ce.toString()), PersistentDataType.STRING)
                        .split(":")[1]);
            }
        } else {
            int index = -1;
            for (String str : item.getItemMeta().getLore()) {
                index++;
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("ce-", "");
                    String[] args = hidden.split(":");
                    if (ce == CustomEnchant.fromString(args[0])) return index;
                }
            }
        }
        return -1;
    }

    public boolean hasProtectionCharm() {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            if (RunesEnchant.is13()) {
                NamespacedKey nk = new NamespacedKey(instance, "re.pc");
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(nk, PersistentDataType.STRING);
            } else {
                for (String str : item.getItemMeta().getLore()) {
                    if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("pc-PROTECTION"))
                        return true;
                }
            }
        }
        return false;
    }

    public int getProtectionCharmLeft() {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("pc-PROTECTION")) {
                    String hiddden = HiddenStringUtils.extractHiddenString(str);
                    String[] args = hiddden.split(":");
                    return Integer.parseInt(args[2]);
                }
            }
        }
        return 0;
    }

    public static boolean isSupportedItem(ItemStack item) {
        Material type = item.getType();
        String[] toCheck = new String[] {"_AXE", "_PICKAXE", "_SWORD", "BOW", "_BOOTS",
        "_HELMET", "_CHESTPLATE", "_LEGGINGS", "_HOE"};

        return Arrays.stream(toCheck).anyMatch(str -> type.toString().endsWith(str));
    }
}