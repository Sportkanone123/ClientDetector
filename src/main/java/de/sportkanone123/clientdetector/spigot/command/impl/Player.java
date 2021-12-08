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

package de.sportkanone123.clientdetector.spigot.command.impl;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class Player {
    public static boolean handle(CommandSender sender, Command command, String label, String[] args) {

        if(args.length >= 3){
            if(Bukkit.getPlayer(args[2]) != null){
                org.bukkit.entity.Player target = Bukkit.getPlayer(args[2]);
                if(args[1].equalsIgnoreCase("client")){
                    if(ClientDetector.playerClient.get(target.getUniqueId()) != null && ClientDetector.clientVersion.get(target.getUniqueId()) == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithoutversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", ClientDetector.playerClient.get(target.getUniqueId()))));
                    }else if(ClientDetector.playerClient.get(target.getUniqueId()) != null && ClientDetector.clientVersion.get(target.getUniqueId()) != null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", ClientDetector.playerClient.get(target.getUniqueId())).replace("%client_version%", ClientDetector.clientVersion.get(target.getUniqueId()))));
                    }else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithoutversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", "Vanilla Minecraft / Undetectable Client")));
                    }
                }else if(args[1].equalsIgnoreCase("mods")){
                    if(ConfigManager.getConfig("message").getString("player.playermods") != null){
                        ArrayList<String> message =  (ArrayList<String>) ConfigManager.getConfig("message").get("player.playermods");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(0).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        if(ClientDetector.playerMods.get(target.getUniqueId()) != null){
                            for(String mod : ClientDetector.playerMods.get(target.getUniqueId())){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(1).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%mod_name%", mod)));
                            }
                        }
                    }
                }else if(args[1].equalsIgnoreCase("version")){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , ConfigManager.getConfig("message").getString("player.mcversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%mc_version%", PacketEvents.get().getPlayerUtils().getClientVersion(target).name().replace("v_", "").replaceAll("_", "."))));
                }else if(args[1].equalsIgnoreCase("labyaddons")){
                    if(ConfigManager.getConfig("message").getString("player.playerlabymods") != null){
                        ArrayList<String> message =  (ArrayList<String>) ConfigManager.getConfig("message").get("player.playermods");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(0).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        if(ClientDetector.playerLabymodMods.get(target.getUniqueId()) != null){
                            for(String mod : ClientDetector.playerLabymodMods.get(target.getUniqueId())){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(1).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%mod_name%", mod)));
                            }
                        }
                    }
                }
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("error.playernotfound").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", args[2])));
            }
        }else if(args.length == 2){
            if(args[1].equalsIgnoreCase("list")){
                if(!Bukkit.getOnlinePlayers().isEmpty()){
                    for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()){
                        if(ClientDetector.playerClient.get(player.getUniqueId()) != null){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientlist").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", player.getName()).replace("%client_name%", ClientDetector.playerClient.get(player.getUniqueId()))));
                        }
                    }
                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("error.noplayersonline").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix"))));
                }
            }
        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("prefix") + " &cPlease use: /cd player <client/list/version/mods/labyaddons> <player>"));
        }
        return false;
    }
}
