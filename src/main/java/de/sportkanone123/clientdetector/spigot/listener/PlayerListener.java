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

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandshake;
import de.sportkanone123.clientdetector.spigot.hackdetector.HackDetector;
import de.sportkanone123.clientdetector.spigot.hackdetector.impl.ChatExploit;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import de.sportkanone123.clientdetector.spigot.manager.GeyserManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {

        if (ClientDetector.plugin.getConfig().getBoolean("forge.simulateForgeHandshake") && !ClientDetector.forgeMods.containsKey(event.getPlayer().getUniqueId()))
            ForgeHandshake.sendHandshake(event.getPlayer());

        de.sportkanone123.clientdetector.spigot.forgemod.newerversion.ForgeHandler.handleJoin(event.getPlayer());

        if (GeyserManager.isBedrockPlayer(event.getPlayer()))
            AlertsManager.handleGeyserDetection(event.getPlayer());

        /* Needed for Cracked Vape detection */
        if(ConfigManager.getConfig("config").getBoolean("hackdetector.detectcrackedvape.enableDetectCrackedVape"))
            event.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8 ");


        if (ClientDetector.plugin.getConfig().getBoolean("client.enableMinecraftVersionDetection")) {
            ClientDetector.mcVersion.put(event.getPlayer().getUniqueId(), PacketEvents.get().getPlayerUtils().getClientVersion(event.getPlayer()).name().replace("v_", "").replaceAll("_", "."));
        }

        if (ConfigManager.getConfig("config").getBoolean("alerts.disablevanillamessages"))
            event.setJoinMessage(null);

        HackDetector.startChatCheck(event.getPlayer());

        if (ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()) != null && !ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()).isEmpty()) {
            List<String> toRemove = new ArrayList<>();
            for (String string : ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId())) {
                toRemove.add(string);
                Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
                    }
                });
            }
            ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()).removeAll(toRemove);
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {

        if (ClientDetector.bungeeManager == null || !ConfigManager.getConfig("config").getBoolean("bungee.enableBungeeClient")) {
            ClientDetector.playerClient.remove(event.getPlayer().getUniqueId());
            ClientDetector.playerMods.remove(event.getPlayer().getUniqueId());
            ClientDetector.playerLabymodMods.remove(event.getPlayer().getUniqueId());
            ClientDetector.forgeMods.remove(event.getPlayer().getUniqueId());
            ClientDetector.mcVersion.remove(event.getPlayer().getUniqueId());
            ClientDetector.clientVersion.remove(event.getPlayer().getUniqueId());
            AlertsManager.firstDetection.remove(event.getPlayer().getUniqueId());
        }

        if(event.getQuitMessage() != null && event.getQuitMessage().contains("Internal Exception: io.netty.codec.DecoderException: Badly compressed packet - size of 2 is below server threshold of 256") || event.getQuitMessage().contains("mismatched mod channel list"))
            ConfigManager.optimizeConfig("config", "forge.simulateForgeHandshake", false);

        if (ConfigManager.getConfig("config").getBoolean("alerts.disablevanillamessages"))
            event.setQuitMessage(null);

        ChatExploit.handleQuit(event.getPlayer());

        if (ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()) != null && !ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()).isEmpty()) {
            ClientDetector.playerCommandsQueue.get(event.getPlayer().getUniqueId()).clear();
        }
    }
}