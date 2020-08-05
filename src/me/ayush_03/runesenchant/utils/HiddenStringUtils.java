package me.ayush_03.runesenchant.utils;

import org.bukkit.ChatColor;

public class HiddenStringUtils {

    private static final String key = generateKey("RUNES");

    public static String encodeString(String s) {
        String hidden = key;
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        return hidden;
    }

    public static boolean hasHiddenString(String s) {
        return s.contains(key);
    }

    public static String extractHiddenString(String s) {
        if (hasHiddenString(s)) {
            int index = s.indexOf(key) + key.length();
            s = s.substring(index);
            s = s.replaceAll(ChatColor.COLOR_CHAR + "", "");
            return s;
        }
        return null;
    }

    private static String generateKey(String raw) {
        String str = "";
        for (char c : raw.toCharArray()) {
            str += ChatColor.COLOR_CHAR + "" + c;
        }
        return str;
    }

}
