package de.sportkanone123.clientdetector.spigot.api.events;

import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ForgeModlistDetectedEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private ModList modlist;

    public ForgeModlistDetectedEvent(Player player, ModList modlist) {
        this.modlist = modlist;
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public ModList getModlist() {
        return modlist;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
