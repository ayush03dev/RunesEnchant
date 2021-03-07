package me.ayushdev.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class RuneConfig {

    private final FileConfiguration fc;
    String section;

    public RuneConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getRuneConfig();
        this.section = ce.toString().toLowerCase();
    }

    public List<String> getLore() {
        if (fc.getStringList(section).isEmpty()) {
            return fc.getStringList("default.lore");
        }

        return fc.getStringList(section + ".lore");
    }

    public String getDisplayName() {
        if (fc.getString(section + ".display-name") == null) {
            return fc.getString("default.display-name");
        }
        return fc.getString(section + ".display-name");
    }

    public int getSuccessRate() {
        if (!fc.isSet(section + ".success-rate")) {
            return getValue("default.success-rate");
        } else {
            return getValue(section + ".success-rate");
        }
    }

    public int getDestroyRate() {
        if (!fc.isSet(section + ".destroy-rate")) {
            return getValue("default.destroy-rate");
        } else {
            return getValue(section + ".destroy-rate");
        }
    }

    public String getItemId() {
        String itemId = fc.getString(section + ".item-id");
        if (itemId == null) {
            return fc.getString("default.item-id");
        }
        return itemId;
    }

    public boolean isGlowing() {
        if (fc.isSet(section + ".glow")) {
            return fc.getBoolean(section + ".glow");
        } else {
            if (fc.isSet("default.glow")) {
                return fc.getBoolean("default.glow");
            }
        }

        return false;
    }

    private int getValue(String path) {
        if (!fc.isSet(path)) return 0;

        if (fc.get(path) instanceof Integer) {
            return fc.getInt(path);
        } else if (fc.get(path) instanceof Double) {
            return (int) fc.getDouble(path);
        } else {
            String str = fc.getString(path);
            if (str.contains("-")) {
                String[] args = str.split("-");
                try {
                    int a = Integer.parseInt(args[0]);
                    int b = Integer.parseInt(args[1]);

                    Random rand = new Random();
                    return rand.ints(a, b + 1).findFirst().getAsInt();
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("ERROR: Rune chances are invalid!");
                    return 0;
                }
            }
        }
        return 0;
    }

}
