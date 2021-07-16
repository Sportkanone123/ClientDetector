package de.sportkanone123.clientdetector.spigot.packet;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;


/*
ClientDetector uses PacketEvents (by retrooper) for packet processing,
License and other information can be found here: https://github.com/retrooper/packetevents/blob/dev/LICENSE
*/


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
