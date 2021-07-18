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

package de.sportkanone123.clientdetector.spigot.mod.processor;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.api.ModDetectEvent;
import de.sportkanone123.clientdetector.spigot.client.Client;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import de.sportkanone123.clientdetector.spigot.mod.Mod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketProcessor {
    public static void handlePacket(Player player, String channel, byte[] data){
        if(ClientDetector.plugin.getConfig().getBoolean("mods.enableModDetection")){
            for(Mod mod : ClientDetector.MODS){
                if(mod.isMod(channel, data)){
                    if(ClientDetector.playerMods.get(player) == null)
                        ClientDetector.playerMods.put(player, new ArrayList<String>());

                    ClientDetector.playerMods.get(player).add(mod.getModName());

                    //Bukkit.getPluginManager().callEvent(new ModDetectEvent(player, mod.getModName()));
                    AlertsManager.handleModlistDetection(player, mod.getModName());

                    if(ClientDetector.plugin.getConfig().getBoolean("mods.enableWhitelist")){
                        if(ClientDetector.plugin.getConfig().get("mods.whitelistedMods") != null){
                            List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("mods.whitelistedMods");
                            if(!whitelist.contains(mod.getModName()))
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("mods.punishCommandWhitelist"));
                        }
                    }

                    if(ClientDetector.plugin.getConfig().getBoolean("mods.enableBlacklist")){
                        if(ClientDetector.plugin.getConfig().get("mods.blacklistedMods") != null){
                            List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("mods.blacklistedMods");
                            if(blacklist.contains(mod.getModName()))
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("mods.punishCommandBlacklist"));
                        }
                    }
                }
            }
        }
    }
}
