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
 *
 * Credits:
 * ClientSocketThread is based on CommandSync by YoFuzzy3.
 * Author: YoFuzzy3, edited by Sportkanone123
 * GitHub: https://github.com/YoFuzzy3/CommandSync
 */

package de.sportkanone123.clientdetector.spigot.bungee;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocketThread extends Thread {
    private final BungeeManager bungeeManager;
    private final InetAddress ip;
    private final Integer port;
    private Boolean connected;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Integer heartbeat;
    private final String name;
    private final String pass;
    private final String version;
    private int failedConnections = 0;
    public boolean isActive;

    public ClientSocketThread(final BungeeManager bungeeManager, final InetAddress ip, final Integer port, final Integer heartbeat,final String version, final String name, final String pass) {
        this.connected = false;
        this.version =  version;
        this.bungeeManager = bungeeManager;
        this.ip = ip;
        this.port = port;
        this.heartbeat = heartbeat;
        this.name = name;
        this.pass = pass;
        this.isActive = true;
        this.connect(false);
    }

    @Override
    public void run() {
        while (this.isActive) {
            if (this.connected) {
                this.out.println("heartbeat");

                if (this.out.checkError()) {
                    this.connected = false;
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aLost connection to the bungee."));

                }else {
                    try {
                        final Integer size = this.bungeeManager.oq.size();
                        Integer count = this.bungeeManager.qc;
                        if (size > count) {
                            for (int i = count; i < size; ++i) {
                                ++count;
                                final String output = this.bungeeManager.oq.get(i);
                                this.out.println(output);
                                //Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &a[" + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort() + "] Sent output - " + output));
                            }
                            this.bungeeManager.qc = count;
                        }

                        while (this.in.ready()) {
                            final String input = this.in.readLine();
                            if (!input.equals("heartbeat")) {
                                //Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &a[" + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort() + "] Received input - " + input));
                                ClientDetector.clientSocket.handleSyncMessage(input);
                            }
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                this.connect(true);
            }
            try {
                Thread.sleep(this.heartbeat);
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void connect(final Boolean sleep) {
        if (sleep) {
            try {
                Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            this.socket = new Socket(this.ip, this.port);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out.println(this.name);

            if (this.in.readLine().equals("invalid")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aThe name " + this.name + " is already connected."));
                this.socket.close();
                return;
            }
            this.out.println(this.pass);

            if (this.in.readLine().equals("invalid")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aThe password you provided is invalid."));
                this.socket.close();
                return;
            }
            this.out.println(this.version);

            if (this.in.readLine().equals("invalid")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aThe client's version of " + this.version + " does not match the server's version of " + this.in.readLine() + "."));
                this.socket.close();
                return;
            }
            this.connected = true;

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aConnected to " + this.ip.getHostName() + ":" + this.port + " under name " + this.name + "."));
        } catch (IOException e2) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aBungee&7) &aCould not connect to the server."));
            this.isActive = false;
        }
    }


}

