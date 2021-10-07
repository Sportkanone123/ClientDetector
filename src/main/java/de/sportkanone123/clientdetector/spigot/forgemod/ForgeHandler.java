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
import de.sportkanone123.clientdetector.spigot.client.Client;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ForgeHandler {
    public static void handleDetection(Player player, String mod){
        if(ClientDetector.plugin.getConfig().getBoolean("forge.blockForge")){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandForge"));
        }

        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableWhitelist")){
            if(ClientDetector.plugin.getConfig().get("forge.whitelistedMods") != null){
                List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.whitelistedMods");
                if(!whitelist.contains(mod)){
                    Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, new Runnable(){
                        @Override
                        public void run() {
                            if(player != null && player.isOnline())
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                           }
                    }, 10l);
                }
            }
        }

        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableBlacklist")){
            if(ClientDetector.plugin.getConfig().get("forge.blacklistedMods") != null){
                List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.blacklistedMods");
                if(blacklist.contains(mod)){
                    Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, new Runnable(){
                        @Override
                        public void run() {
                            if(player != null && player.isOnline())
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                        }
                    }, 10l);
                }
            }
        }
    }
}
