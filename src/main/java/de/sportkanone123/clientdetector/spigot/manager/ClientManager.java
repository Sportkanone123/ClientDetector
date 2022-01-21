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

package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.bungee.DataType;
import de.sportkanone123.clientdetector.spigot.client.Client;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientManager {

    public static void load(){

        /*
        Tested
         */
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("FML|HS", "l:fmlhs"), "", "Forge", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("5zig_Set", "l:5zig_set"), "", "5zig Mod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("LABYMOD", "LMC", "labymod3:main"), "", "LabyMod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("vanilla", "vanilla", "Aristois (Experimental)", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client("vivecraft:data", "", "Vivecraft", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("mysterymod:mm", "", "MysteryMod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("hyperium", "", "Hyperium", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("PX|Version", "", "PXMod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("MC|Pixel", "", "Pixel Client", true, false, null, null));

        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "vanilla", "Vanilla (Undetectable)", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "LiteLoader", "LiteLoader", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "PLC18", "PvPLounge Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "fabric", "Fabric", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "lunarclient", "Lunar Client", false, true, ":", 1));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), Arrays.asList("forge", "fml", "fml,forge"), "Forge", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "PolarClient", "Polar Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "rift", "Rift", false, false, null, null));

        ClientDetector.CLIENTS.add(new Client("REGISTER", "CB-Client", "CheatBreaker", false, false, null, null));


        /*
        Not tested
         */
        ClientDetector.CLIENTS.add(new Client("LOLIMAHACKER", "", "Cracked Vape", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("LC|Brand", "", "Winterware", true, false, null, null));

        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "Subsystem", "Easy Minecraft Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "Minecraft-Console-Client", "Console Client", false, true, "/", 1));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand"), "Vanilla", "Jigsaw", false, false, null, null));
    }

    public static void unLoad(){
        ClientDetector.CLIENTS = new ArrayList<Client>();
    }

    public static void handleDetection(Player player, String client){
        if(ClientDetector.bungeeManager != null && ConfigManager.getConfig("config").getBoolean("bungee.enableBungeeClient")){
            ClientDetector.bungeeManager.syncList(DataType.CLIENT_LIST, player, client);
        }

        if(ClientDetector.plugin.getConfig().getBoolean("client.enableWhitelist")){
            if(ClientDetector.plugin.getConfig().get("client.whitelistedClients") != null){
                List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("client.whitelistedClients");
                if(!whitelist.contains(client) && !player.hasPermission("clientdetector.bypass")) {
                    if(player.isOnline()){
                        Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("client.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%client_name%", client).replace("%player_uuid%", player.getUniqueId().toString()));
                            }
                        });
                    }else{
                        if(ClientDetector.playerCommandsQueue.get(player.getUniqueId()) == null)
                            ClientDetector.playerCommandsQueue.put(player.getUniqueId(), new ArrayList<>());

                        ClientDetector.playerCommandsQueue.get(player.getUniqueId()).add(ClientDetector.plugin.getConfig().getString("client.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%client_name%", client).replace("%player_uuid%", player.getUniqueId().toString()));
                    }
                }
            }
        }

        if(ClientDetector.plugin.getConfig().getBoolean("client.enableBlacklist")){
            if(ClientDetector.plugin.getConfig().get("client.blacklistedClients") != null){
                List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("client.blacklistedClients");
                if(blacklist.contains(client) && !player.hasPermission("clientdetector.bypass")){
                    if(player.isOnline()){
                        Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("client.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%client_name%", client).replace("%player_uuid%", player.getUniqueId().toString()));
                            }
                        });
                    }else{
                        if(ClientDetector.playerCommandsQueue.get(player.getUniqueId()) == null)
                            ClientDetector.playerCommandsQueue.put(player.getUniqueId(), new ArrayList<>());

                        ClientDetector.playerCommandsQueue.get(player.getUniqueId()).add(ClientDetector.plugin.getConfig().getString("client.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%client_name%", client).replace("%player_uuid%", player.getUniqueId().toString()));
                    }
                }
            }
        }
    }
}
