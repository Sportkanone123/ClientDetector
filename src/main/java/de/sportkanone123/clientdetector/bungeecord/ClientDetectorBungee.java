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

package de.sportkanone123.clientdetector.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientDetectorBungee extends Plugin implements Listener {
    public static Plugin plugin;
    private Map<ServerInfo, List<byte[]>> queue = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        ProxyServer.getInstance().registerChannel("cd:bungee");

        runQueue();
    }

    public void runQueue() {
        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                for(ServerInfo serverInfo : queue.keySet()){
                    if(!serverInfo.getPlayers().isEmpty() && queue.get(serverInfo) != null){
                        List<byte[]> toRemove = new ArrayList<>();
                        for(byte[] data : queue.get(serverInfo)){
                            toRemove.add(data);
                            serverInfo.sendData("cd:spigot", data);
                        }
                        queue.get(serverInfo).removeAll(toRemove);
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if(event.getTag().equalsIgnoreCase("cd:bungee")){
            sync(event.getData());
        }
    }

    private void sync(String string){
        sync(string.getBytes(StandardCharsets.UTF_8));
    }

    private void sync(byte[] data){
        for(ServerInfo server : ProxyServer.getInstance().getServers().values()){
            if(server.getPlayers().isEmpty()){
                if(queue.get(server) == null) queue.put(server, new ArrayList<>());

                if(!new String(data, StandardCharsets.UTF_8).contains("CROSS_SERVER_MESSAGE"))
                    queue.get(server).add(data);
            }else{
                server.sendData("cd:spigot", data);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        String placeholder = "@@";
        sync("PLAYER_LEFT" + placeholder + e.getPlayer().getName());
    }
}
