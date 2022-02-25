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

package de.sportkanone123.clientdetector.spigot.hackdetector;

import de.sportkanone123.clientdetector.spigot.hackdetector.impl.AntiFastMath;
import de.sportkanone123.clientdetector.spigot.hackdetector.impl.ChatExploit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HackDetector implements Listener {
    public static void startChatCheck(Player player){
        ChatExploit.startDetection(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        ChatExploit.handleChat(event.getPlayer(), event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        ChatExploit.handleMovement(event.getPlayer(), event);
        AntiFastMath.handleMovement(event.getPlayer(), event);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        AntiFastMath.handleJoin(event.getPlayer(), event);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        AntiFastMath.handleQuit(event.getPlayer(), event);
    }
}
