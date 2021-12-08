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
 * ServerSocketHandler is based on CommandSync by YoFuzzy3.
 * Author: YoFuzzy3, edited by Sportkanone123
 * GitHub: https://github.com/YoFuzzy3/CommandSync
 */

package de.sportkanone123.clientdetector.bungeecord.utils;

import de.sportkanone123.clientdetector.bungeecord.ClientDetectorBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ServerSocketHandler extends Thread
{
    private final ClientDetectorBungee plugin;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Integer heartbeat;
    private final String name;
    private final String pass;
    private final String version;

    public ServerSocketHandler(final ClientDetectorBungee plugin, final Socket socket, final Integer heartbeat, final String version, final String pass) throws IOException {
        this.version = version;
        this.plugin = plugin;
        this.socket = socket;
        this.heartbeat = heartbeat;
        this.pass = pass;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        System.out.println("[ClientDetector] (Socket) Received new connection from " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + ".");

        final String receivedName = this.in.readLine();
        this.name = receivedName;
        if (plugin.c.contains(receivedName)) {
            System.out.println("[ClientDetector] (Socket) [" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "] [" + receivedName + "] Provided a name that is already connected.");
            this.out.println("invalid");
            socket.close();
            return;
        }
        this.out.println("valid");

        final String receivedPass = this.in.readLine();
        if (!receivedPass.equals(this.pass)) {
            System.out.println("[ClientDetector] (Socket) [" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "] [" + receivedName + "] Provided an invalid password.");
            this.out.println("invalid");
            socket.close();
            return;
        }
        this.out.println("valid");

        final String receivedVersion = this.in.readLine();
        if (!receivedVersion.equals(this.version)) {
            System.out.println("[ClientDetector] (Socket) [" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "] [" + receivedName + "] Client's version of " + receivedVersion + " does not match the server's version of " + this.version + ".");
            this.out.println("invalid");
            socket.close();
            return;
        }
        this.out.println("valid");

        if (!plugin.qc.containsKey(receivedName)) {
            plugin.qc.put(receivedName, 0);
        }

        plugin.c.add(receivedName);
        System.out.println("[ClientDetector] (Socket) Connection from " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " under name " + receivedName + " has been authorised.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    this.out.println("heartbeat");
                    if (this.out.checkError()) {
                        break;
                    }

                    while (this.in.ready()) {
                        final String input = this.in.readLine();

                        if (!input.equals("heartbeat")) {
                            //System.out.println("[ClientDetector] (Socket)  [" + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort() + "] [" + this.name + "] Received input - " + input);
                            plugin.handleSyncMessage(input, this.name);
                        }
                    }


                    final Integer size = this.plugin.oq.size();
                    Integer count = this.plugin.qc.get(this.name);
                    if (size > count) {
                        for (int i = count; i < size; ++i) {
                            ++count;
                            final String output = this.plugin.oq.get(i);

                            this.out.println(output);
                            //System.out.println("[" + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort() + "] [" + this.name + "] Sent output - " + output);
                        }
                        this.plugin.qc.put(this.name, count);
                    }

                    Thread.sleep(this.heartbeat);
                }

                //System.out.println("[ClientDetector] (Socket)  Connection from " + this.socket.getInetAddress().getHostName() + ":" + this.socket.getPort() + " under name " + this.name + " has disconnected.");
                this.plugin.c.remove(this.name);
            }
            catch (Exception e) {
                this.plugin.c.remove(this.name);
                e.printStackTrace();
                continue;
            }
            break;
        }
    }
}
