package me.ayushdev.runesenchant.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EnchanterItemSetEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();

    Player p;
    ItemStack item;
    boolean cancelled;

    public EnchanterItemSetEvent(Player p, ItemStack item) {
        this.p = p;
        this.item = item;
    }

    public Player getPlayer() {
        return p;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean flag) {
        this.cancelled = flag;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}