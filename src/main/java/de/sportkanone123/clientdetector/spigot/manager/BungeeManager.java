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

import de.sportkanone123.clientdetector.bungeecord.utils.CustomPayload;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class BungeeManager {
    public static void handle(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("clientdetector:fix")){
            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(data));

            String uuid = "";
            String channelMessage = "";
            String dataMessage = "";

            try {
                uuid = msgin.readUTF();
                channelMessage = msgin.readUTF();
                dataMessage = msgin.readUTF();
            } catch (IOException e) {
                return;
            }

            if(ClientDetector.bungeePayload.get(UUID.fromString(uuid)) == null)
                ClientDetector.bungeePayload.put(UUID.fromString(uuid) , new ArrayList<CustomPayload>());

            ClientDetector.bungeePayload.get(UUID.fromString(uuid)).add(new CustomPayload(UUID.fromString(uuid), channelMessage, dataMessage.getBytes(StandardCharsets.UTF_8)));
        }
    }
}
