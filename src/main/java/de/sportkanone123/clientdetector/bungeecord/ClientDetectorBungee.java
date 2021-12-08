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

import de.sportkanone123.clientdetector.bungeecord.utils.FileUtils;
import de.sportkanone123.clientdetector.bungeecord.utils.ServerSocketListener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetAddress;
import java.net.ServerSocket;

import java.util.*;

public class ClientDetectorBungee extends Plugin implements Listener {
    public ServerSocket server;
    public Set<String> c;
    public List<String> oq;
    public Map<String, List<String>> pq;
    public Map<String, Integer> qc;
    public static Plugin plugin;

    public ClientDetectorBungee() {
        this.c = Collections.synchronizedSet(new HashSet<String>());
        this.oq = Collections.synchronizedList(new ArrayList<String>());
        this.pq = Collections.synchronizedMap(new HashMap<String, List<String>>());
        this.qc = Collections.synchronizedMap(new HashMap<String, Integer>());
    }


    @Override
    public void onEnable() {
        plugin = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        FileUtils.loadConfig("bungeeconfig");

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        try {
            this.server = new ServerSocket(FileUtils.getConfig("bungeeconfig").getInt("sync.syncPort"), 50, InetAddress.getByName("localhost"));
            new ServerSocketListener(this, FileUtils.getConfig("bungeeconfig").getInt("sync.heartbeat"), "1.0", FileUtils.getConfig("bungeeconfig").getString("sync.syncPassword")).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSyncMessage(String string, String name){
        this.oq.add(string);
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        String placeholder = "@@";

        this.oq.add("PLAYER_LEFT" + placeholder + e.getPlayer().getName());
    }
}
