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

package de.sportkanone123.clientdetector.spigot.client.processor;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.client.Client;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import org.bukkit.entity.Player;

public class PacketProcessor {
    public static void handlePacket(Player player, String channel, byte[] data){
        for(Client client : ClientDetector.CLIENTS){
            if(client.isClient(channel, data) && ClientDetector.plugin.getConfig().getBoolean("client.enableClientDetection")){
                if(client.getClientName() == "Vanilla Minecraft / Undetectable Client"){
                    if(ClientDetector.playerClient.get(player) == null){
                        ClientDetector.playerClient.put(player, client.getClientName());

                        if(ClientDetector.mcVersion.get(player) != null && client.getClientName() != null)
                            ClientDetector.clientVersion.put(player, ClientDetector.mcVersion.get(player));

                        AlertsManager.handleClientDetection(player);

                    }
                }else{
                    if(ClientDetector.playerClient.get(player) == null || ClientDetector.playerClient.get(player) == "Vanilla Minecraft / Undetectable Client" || ClientDetector.playerClient.get(player) == "Unknown Client (Not Vanilla Minecraft)"){
                        ClientDetector.playerClient.put(player, client.getClientName());

                        if(client.getHasVersion() && client.getVersion(channel, data) != null && ClientDetector.plugin.getConfig().getBoolean("client.enableVersionDetection"))
                            ClientDetector.clientVersion.put(player, client.getVersion(channel, data));
                        else
                            ClientDetector.clientVersion.put(player, null);

                        AlertsManager.handleClientDetection(player);

                    }
                }
            }
        }
    }
}
