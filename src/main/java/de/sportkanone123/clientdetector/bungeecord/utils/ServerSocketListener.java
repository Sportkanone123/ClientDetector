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
 * ServerSocketListener is based on CommandSync by YoFuzzy3.
 * Author: YoFuzzy3, edited by Sportkanone123
 * GitHub: https://github.com/YoFuzzy3/CommandSync
 */

package de.sportkanone123.clientdetector.bungeecord.utils;

import de.sportkanone123.clientdetector.bungeecord.ClientDetectorBungee;

import java.io.IOException;

public class ServerSocketListener extends Thread {
    private final ClientDetectorBungee plugin;
    private final Integer heartbeat;
    private final String version;
    private final String pass;

    public ServerSocketListener(final ClientDetectorBungee plugin, final Integer heartbeat, final String version, final String pass) {
    this.plugin = plugin;
    this.heartbeat = heartbeat;
    this.version = version;
    this.pass = pass;
}

    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    new ServerSocketHandler(this.plugin, this.plugin.server.accept(), this.heartbeat, this.version, this.pass).start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }
    }
}
