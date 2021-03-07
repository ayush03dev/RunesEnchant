package me.ayushdev.runesenchant;

import org.bukkit.entity.Player;

public class MessageManager {

    private static final MessageManager instance = new MessageManager();

    public static MessageManager getInstance() {
        return instance;
    }

    public void sendMessage(Player p, Message message) {
        p.sendMessage(message.toString());
    }

    public void sendMessage(Player p, Message message, Placeholder... holders) {
        String msg = message.toString();

        if (msg == null) return;

        for (Placeholder ph: holders) {
            msg = msg.replace(ph.getName(), ph.getData().toString());
        }

        p.sendMessage(msg);
    }

}
