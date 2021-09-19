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

package de.sportkanone123.clientdetector.spigot.listener;

import de.sportkanone123.clientdetector.spigot.packet.Packet;
import de.sportkanone123.clientdetector.spigot.packet.processor.PacketProcessor;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketLoginReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import org.bukkit.event.Listener;

import java.nio.charset.StandardCharsets;


public class NetworkListener extends PacketListenerDynamic implements Listener {

    public NetworkListener() {
        super(PacketEventPriority.NORMAL);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PacketProcessor.handlePacket(event.getPlayer(), new Packet(event.getNMSPacket(), event.getPacketId()));
    }

    @Override
    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
        PacketProcessor.handleLoginPacket(event);
    }

    /*@Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if(event.getPacketId() == PacketType.Play.Server.CUSTOM_PAYLOAD){
            WrappedPacketOutCustomPayload wrappedPacketInCustomPayload = new WrappedPacketOutCustomPayload(event.getNMSPacket());

            System.out.println("-----------[Packet S -> C]-----------");
            System.out.println("Player: " + event.getPlayer());
            System.out.println("Channel: '" + wrappedPacketInCustomPayload.getChannelName() + "'");
            System.out.println("Data: '" + new String(wrappedPacketInCustomPayload.getData(), StandardCharsets.UTF_8) + "'");
            System.out.println("-----------[Packet S -> C]-----------");
        }
    }*/
}
