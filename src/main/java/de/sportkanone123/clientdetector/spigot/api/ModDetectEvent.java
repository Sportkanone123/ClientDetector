package de.sportkanone123.clientdetector.spigot.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModDetectEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private String modName;

    public ModDetectEvent(Player player, String modName) {
        this.modName = modName;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMod() {
        return modName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
