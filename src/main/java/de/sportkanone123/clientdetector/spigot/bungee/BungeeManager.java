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

package de.sportkanone123.clientdetector.spigot.bungee;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class BungeeManager {
    public ClientSocketThread client;
    public List<String> oq;
    public Integer qc;
    public String placeholder = "@@";

    public BungeeManager() {
        this.oq = Collections.synchronizedList(new ArrayList<String>());
        this.qc = 0;
    }

    public void load(){
        try {
            (client = new ClientSocketThread(this,
                    InetAddress.getByName(ConfigManager.getConfig("config").getString("bungee.bungeeIPAdress")),
                    ConfigManager.getConfig("config").getInt("bungee.bungeePort"),
                    ConfigManager.getConfig("config").getInt("bungee.heartbeat"),
                    "1.0",
                    ConfigManager.getConfig("config").getString("bungee.serverName"),
                    ConfigManager.getConfig("config").getString("bungee.bungeePassword"))
            ).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncList(DataType type, Player player, ArrayList<String> list){
        String str = type.toString() + placeholder + player.getUniqueId();

        for(int i = 0; i < list.size(); i++)
            str = str + placeholder + list.get(i);

        this.oq.add(str);
    }
    public void syncList(DataType type, Player player, String client){
        String str = type.toString() + placeholder + player.getUniqueId() + placeholder + client;

        this.oq.add(str);
    }

    public void syncList(DataType type, String message){
        String str = type.toString() + placeholder + ConfigManager.getConfig("config").getString("bungee.serverName") + placeholder + message;

        this.oq.add(str);
    }

    public void handleSyncMessage(String string){
        String[] strings = string.split(placeholder);


        if(strings[0].equalsIgnoreCase("CLIENT_LIST") && strings.length == 3) {
            ClientDetector.playerClient.put(UUID.fromString(strings[1]), strings[2]);

        }else if(strings[0].equalsIgnoreCase("MOD_LIST") && strings.length >= 3) {
            ArrayList<String> list = new ArrayList<>();

            for(int i = 2; i < strings.length; i++)
                list.add(strings[i]);

            ClientDetector.playerMods.put(UUID.fromString(strings[1]), list);

        }else if(strings[0].equalsIgnoreCase("FORGE_MOD_LIST") && strings.length >= 3) {
            ArrayList<String> list = new ArrayList<>();

            for(int i = 2; i < strings.length; i++)
                list.add(strings[i]);

            ClientDetector.forgeMods.put(UUID.fromString(strings[1]), new ModList(list));

        }else if(strings[0].equalsIgnoreCase("CROSS_SERVER_MESSAGE") && strings.length == 3) {
            String message = strings[1];
            String server = strings[2];

            AlertsManager.handleCrossServer(server, message);

        }else if(strings[0].equalsIgnoreCase("PLAYER_LEFT") && strings.length == 2) {
            UUID uuid;

            if(Bukkit.getPlayer(strings[1]) != null){
                uuid = Bukkit.getPlayer(strings[1]).getUniqueId();
            }else
                uuid = Bukkit.getOfflinePlayer(strings[1]).getUniqueId();


            ClientDetector.playerClient.remove(uuid);
            ClientDetector.playerMods.remove(uuid);
            ClientDetector.playerLabymodMods.remove(uuid);
            ClientDetector.forgeMods.remove(uuid);
            ClientDetector.mcVersion.remove(uuid);
            ClientDetector.clientVersion.remove(uuid);
            AlertsManager.firstDetection.remove(uuid);
        }
    }
}
