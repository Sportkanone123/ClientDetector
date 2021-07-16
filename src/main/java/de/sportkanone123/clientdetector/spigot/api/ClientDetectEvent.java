package de.sportkanone123.clientdetector.spigot.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientDetectEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private String client;

    public ClientDetectEvent(Player player, String client) {
        this.client = client;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public String getClient() {
        return client;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
