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

package de.sportkanone123.clientdetector.spigot.forgemod;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.api.events.ClientDetectedEvent;
import de.sportkanone123.clientdetector.spigot.bungee.DataType;
import de.sportkanone123.clientdetector.spigot.client.Client;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ForgeHandler {
    public static void handlePluginMessage(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("FML|HS") || channel.equalsIgnoreCase("l:fmlhs") || (channel.equalsIgnoreCase("minecraft:brand") && new String(data).contains("forge")) || (channel.equalsIgnoreCase("MC|Brand") && new String(data).contains("forge"))){
            if(ConfigManager.getConfig("config").getBoolean("forge.blockForge")){
                if(!player.hasPermission("clientdetector.bypass") && !((ArrayList<String>) ConfigManager.getConfig("config").get("forge.whitelistedPlayers")).contains(player.getName())){
                    if(player.isOnline()){
                        Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, new Runnable(){
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getConfig("config").getString("forge.punishCommandForge").replace("%player_name%", player.getName()));
                            }
                        }, 10l);
                    }else{
                        if(ClientDetector.playerCommandsQueue.get(player.getUniqueId()) == null)
                            ClientDetector.playerCommandsQueue.put(player.getUniqueId(), new ArrayList<>());

                        ClientDetector.playerCommandsQueue.get(player.getUniqueId()).add(ConfigManager.getConfig("config").getString("forge.punishCommandForge").replace("%player_name%", player.getName()));
                    }
                }
            }
        }
    }

    public static void handleDetection(Player player, String mod){
        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableWhitelist")){
            if(ClientDetector.plugin.getConfig().get("forge.whitelistedMods") != null){
                List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.whitelistedMods");
                if(!whitelist.contains(mod) && !player.hasPermission("clientdetector.bypass") && !((ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.whitelistedPlayers")).contains(player.getName())){
                    if(player.isOnline()){
                        Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, new Runnable(){
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                            }
                        }, 10l);
                    }else{
                        if(ClientDetector.playerCommandsQueue.get(player.getUniqueId()) == null)
                            ClientDetector.playerCommandsQueue.put(player.getUniqueId(), new ArrayList<>());

                        ClientDetector.playerCommandsQueue.get(player.getUniqueId()).add(ClientDetector.plugin.getConfig().getString("forge.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                    }
                }
            }
        }

        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableBlacklist")){
            if(ClientDetector.plugin.getConfig().get("forge.blacklistedMods") != null){
                List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.blacklistedMods");
                if(blacklist.contains(mod) && !player.hasPermission("clientdetector.bypass") && !((ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.whitelistedPlayers")).contains(player.getName())){
                    if(player.isOnline()){
                        Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, new Runnable(){
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                            }
                        }, 10l);
                    }else{
                        if(ClientDetector.playerCommandsQueue.get(player.getUniqueId()) == null)
                            ClientDetector.playerCommandsQueue.put(player.getUniqueId(), new ArrayList<>());

                        ClientDetector.playerCommandsQueue.get(player.getUniqueId()).add(ClientDetector.plugin.getConfig().getString("forge.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                    }
                }
            }
        }
    }
}
