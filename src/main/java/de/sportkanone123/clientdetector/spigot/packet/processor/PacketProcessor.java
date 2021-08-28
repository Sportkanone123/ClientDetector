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

import de.sportkanone123.clientdetector.spigot.packet.Packet;
import io.github.retrooper.packetevents.event.impl.PacketLoginReceiveEvent;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class PacketProcessor {

    public static void handlePacket(Player player, Packet packet){
        if(packet.isCustomPayload()){
            WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());

            String channel = wrappedPacketInCustomPayload.getChannelName();
            byte[] data = wrappedPacketInCustomPayload.getData();

            de.sportkanone123.clientdetector.spigot.client.processor.PacketProcessor.handlePacket(player, channel, data);
            de.sportkanone123.clientdetector.spigot.mod.processor.PacketProcessor.handlePacket(player, channel, data);

            de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(player, channel, data);

            de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePacket(player, channel, data);

            /*System.out.println("-----------[Packet C -> S]-----------");
            System.out.println("Player: " + player);
            System.out.println("Channel: '" + wrappedPacketInCustomPayload.getChannelName() + "'");
            System.out.println("Data: '" + new String(data, StandardCharsets.UTF_8) + "'");
            System.out.println("-----------[Packet C -> S]-----------");*/
        }
    }

    public static void handleLoginPacket(PacketLoginReceiveEvent event){
        de.sportkanone123.clientdetector.spigot.forgemod.newerversion.ForgeHandler.handle(event);
    }

}
