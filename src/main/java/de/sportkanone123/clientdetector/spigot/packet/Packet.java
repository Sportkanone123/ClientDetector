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

package de.sportkanone123.clientdetector.spigot.packet;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;

public class Packet {
    private final NMSPacket rawPacket;
    private final byte packetId;

    public Packet(NMSPacket rawPacket, byte packetId) {
        this.rawPacket = rawPacket;
        this.packetId = packetId;
    }

    public NMSPacket getRawPacket(){ return rawPacket; }

    public byte getPacketId(){ return packetId; }

    public boolean isCustomPayload() { return packetId == PacketType.Play.Client.CUSTOM_PAYLOAD; }
}
