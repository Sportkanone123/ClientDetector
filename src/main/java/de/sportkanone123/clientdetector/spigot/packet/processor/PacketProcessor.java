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

package de.sportkanone123.clientdetector.spigot.packet.processor;

import com.github.retrooper.packetevents.event.simple.PacketLoginReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class PacketProcessor {

    public static void handlePacket(PacketPlayReceiveEvent event){
        if(event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE){
            WrapperPlayClientPluginMessage wrapperPlayClientPluginMessage = new WrapperPlayClientPluginMessage(event);

            if(event.getPlayer() != null && ((Player) event.getPlayer()) != null && ((Player) event.getPlayer()).getUniqueId() != null){
                Player player = (Player) event.getPlayer();
                String channel = wrapperPlayClientPluginMessage.getChannelName();
                byte[] data = wrapperPlayClientPluginMessage.getData();

                de.sportkanone123.clientdetector.spigot.client.processor.PacketProcessor.handlePacket(player, channel, data);
                de.sportkanone123.clientdetector.spigot.mod.processor.PacketProcessor.handlePacket(player, channel, data);

                de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(player, channel, data);

                de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePacket(player, channel, data);
                de.sportkanone123.clientdetector.spigot.forgemod.ForgeHandler.handlePluginMessage(player, channel, data);
            }
        }
    }

    public static void handleLoginPacket(PacketLoginReceiveEvent event){
        de.sportkanone123.clientdetector.spigot.forgemod.newerversion.ForgeHandler.handle(event);
    }

}
