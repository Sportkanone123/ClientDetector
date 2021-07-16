package de.sportkanone123.clientdetector.spigot.packet.processor;

import de.sportkanone123.clientdetector.spigot.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import org.bukkit.entity.Player;


/*
ClientDetector uses PacketEvents (by retrooper) for packet processing,
License and other information can be found here: https://github.com/retrooper/packetevents/blob/dev/LICENSE
*/


public class PacketProcessor {

    public static void handlePacket(Player player, Packet packet){
        if(packet.isCustomPayload()){
            WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());

            String channel = wrappedPacketInCustomPayload.getChannelName();
            byte[] data = wrappedPacketInCustomPayload.getData();

            de.sportkanone123.clientdetector.spigot.client.processor.PacketProcessor.handlePacket(player, channel, data);
            de.sportkanone123.clientdetector.spigot.mod.processor.PacketProcessor.handlePacket(player, channel, data);

            de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(player, channel, data);
            de.sportkanone123.clientdetector.spigot.forgemod.newerversion.ForgeHandler.handle(player, packet);

            de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePacket(player, channel, data);

            /*System.out.println("-----------[Packet]-----------");
            System.out.println("Player: " + player);
            System.out.println("Channel: '" + wrappedPacketInCustomPayload.getChannelName() + "'");
            System.out.println("Data: '" + new String(data, StandardCharsets.UTF_8) + "'");
            System.out.println("-----------[Packet]-----------");*/
        }
    }
}
