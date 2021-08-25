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

package de.sportkanone123.clientdetector.spigot.listener;

import de.sportkanone123.clientdetector.bungeecord.utils.CustomPayload;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandshake;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import de.sportkanone123.clientdetector.spigot.manager.GeyserManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event){
        ClientDetector.playerMods.put(event.getPlayer(), new ArrayList<String>());
        ClientDetector.clientVersion.put(event.getPlayer(), null);
        ClientDetector.playerMods.put(event.getPlayer(), null);
        ClientDetector.playerLabymodMods.put(event.getPlayer(), new ArrayList<String>());

        if(ClientDetector.plugin.getConfig().getBoolean("forge.simulateForgeHandshake"))
            ForgeHandshake.sendHandshake(event.getPlayer());

        de.sportkanone123.clientdetector.spigot.forgemod.newerversion.ForgeHandler.handleJoin(event.getPlayer());

        if(GeyserManager.isBedrockPlayer(event.getPlayer()))
            AlertsManager.handleGeyserDetection(event.getPlayer());

        /* Needed for Cracked Vape detection */
        event.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8 ");

        if(ClientDetector.plugin.getConfig().getBoolean("client.enableMinecraftVersionDetection")){
            ClientDetector.mcVersion.put(event.getPlayer(), PacketEvents.get().getPlayerUtils().getClientVersion(event.getPlayer()).name().replace("v_", "").replaceAll("_", "."));
        }

        if(ClientDetector.bungeePayload.get(event.getPlayer().getUniqueId()) != null){
            for(CustomPayload customPayload : ClientDetector.bungeePayload.get(event.getPlayer().getUniqueId())){
                de.sportkanone123.clientdetector.spigot.client.processor.PacketProcessor.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());
                de.sportkanone123.clientdetector.spigot.mod.processor.PacketProcessor.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());

                de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());

                de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());
            }
        }

        if(ConfigManager.getConfig("config").getBoolean("alerts.disablevanillamessages"))
            event.setJoinMessage(null);
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event){

        ClientDetector.bungeePayload.put(event.getPlayer().getUniqueId(), new ArrayList<CustomPayload>());

        if(ConfigManager.getConfig("config").getBoolean("alerts.disablevanillamessages"))
            event.setQuitMessage(null);
    }

    /*@EventHandler
    public static void onChat(AsyncPlayerChatEvent event){
        ClientDisabler.handleChatEvent(event);
    }*/
}
