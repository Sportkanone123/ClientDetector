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

package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
More information can be found here: https://github.com/Pokechu22/WorldDownloader-Serverside-Companion
 */
public class WorldDownloader {
    public static void handle(Player player){
        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeInt(0);
            msgout.writeBoolean(!ConfigManager.getConfig("clientcontrol").getBoolean("worlddownloader.disableAll"));
        } catch (IOException exception){

        }

        WrapperPlayServerPluginMessage costumPayload;

        if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)){
            costumPayload = new WrapperPlayServerPluginMessage("wdl:control", msgbytes.toByteArray());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
        }else{
            costumPayload = new WrapperPlayServerPluginMessage("WDL|CONTROL", msgbytes.toByteArray());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
        }
    }
}
