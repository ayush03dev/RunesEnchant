package me.ayushdev.runesenchant.events;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.LuckStone;
import me.ayushdev.runesenchant.ResurrectionStone;
import me.ayushdev.runesenchant.Rune;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RuneApplyEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    Player p;
    Rune rune;
    ApplicableItem item;
    boolean enchanter;
    ResurrectionStone rs;
    LuckStone ls;
    boolean cancelled;

    public RuneApplyEvent(Player p, ApplicableItem item, Rune rune, boolean enchanter, ResurrectionStone rs,
                          LuckStone ls) {
        this.p = p;
        this.item = item;
        this.rune = rune;
        this.enchanter = enchanter;
        this.rs = rs;
        this.ls = ls;
    }

    public Player getPlayer() {
        return p;
    }

    public Rune getRune() {
        return rune;
    }

    public ApplicableItem getItem() {
        return item;
    }

    public boolean isEnchanter() {
        return enchanter;
    }

    public ResurrectionStone getResurrectionStone() {
        return rs;
    }

    public LuckStone getLuckStone() {
        return ls;
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
