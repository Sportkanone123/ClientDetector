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

import de.sportkanone123.clientdetector.bungeecord.utils.CustomPayload;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ClientDetectorBungee extends Plugin implements Listener {
    HashMap<ProxiedPlayer, ArrayList<CustomPayload>> sentData = new HashMap<ProxiedPlayer, ArrayList<CustomPayload>>();

    @Override
    public void onEnable() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, this);

        ProxyServer.getInstance().registerChannel("clientdetector:fix");
        ProxyServer.getInstance().registerChannel("clientdetector:sync");
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event){
        sentData.put(event.getPlayer(), new ArrayList<CustomPayload>());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event){
        sentData.put(event.getPlayer(), null);
    }

    @EventHandler
    public void onPlayerSwitchServer(ServerConnectedEvent event){
        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            public void run() {
                for(CustomPayload customPayload : sentData.get(event.getPlayer())) {
                    ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                    DataOutputStream msgout = new DataOutputStream(msgbytes);

                    try {
                        msgout.writeUTF(customPayload.getUuid().toString());
                        msgout.writeUTF(customPayload.getChannel().toString());
                        msgout.writeUTF(new String(customPayload.getData(), StandardCharsets.UTF_8));
                    } catch (IOException exception) {

                    }

                    event.getServer().sendData("clientdetector:fix", msgbytes.toByteArray());
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        String channel = event.getTag();
        byte[] data = event.getData();
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            sentData.get(player).add(new CustomPayload(player.getUniqueId(), channel, data));
        }else{
            if(channel.equalsIgnoreCase("clientdetector:sync")){
                for(ServerInfo server : ProxyServer.getInstance().getServers().values()) {
                    server.sendData(channel, data);
                }
            }
        }
    }
}
