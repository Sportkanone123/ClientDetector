/*
 * This file is part of ClientDetector - https://github.com/Sportkanone123/ClientDetector
 * Copyright (C) 2021 Sportkanone123
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
