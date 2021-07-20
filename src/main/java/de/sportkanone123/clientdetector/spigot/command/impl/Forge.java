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
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Forge {
    public static boolean handle(CommandSender sender, Command command, String label, String[] args) {
        org.bukkit.entity.Player p = (Player) sender;
        if(args.length == 2){
            if(Bukkit.getPlayer(args[1]) != null){
                org.bukkit.entity.Player target = Bukkit.getPlayer(args[1]);

                if(ClientDetector.playerClient.get(target) != null && ClientDetector.playerClient.get(target) == "Forge"){
                    if(ClientDetector.forgeMods != null && ClientDetector.forgeMods.get(target) != null && ClientDetector.forgeMods.get(target).getMods() != null && ClientDetector.forgeMods.get(target).getMods().size() != 0){
                        ArrayList<String> message =  (ArrayList<String>) ConfigManager.getConfig("message").get("forge.usingforgemodlist");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(0).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(1).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(2).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));

                        for(String mod : ClientDetector.forgeMods.get(target).getMods().keySet()){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(3).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%mod_name%", mod).replace("%mod_version%", ClientDetector.forgeMods.get(target).getMods().get(mod))));
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(4).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                    }else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("forge.usingforgebutnomodlistreceived").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", args[1])));
                    }
                }else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("forge.notusingforge").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", args[1])));
                }
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("error.playernotfound").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", args[1])));
            }
        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("prefix") + " &cPlease use: /cd forge <player>"));
        }
        return false;
    }
}
