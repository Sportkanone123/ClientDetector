package de.sportkanone123.clientdetector.spigot.listener;
import com.comphenix.protocol.events.PacketContainer;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.packet.Packet;
import de.sportkanone123.clientdetector.spigot.packet.processor.PacketProcessor;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class NetworkListener extends PacketListenerDynamic {

    public NetworkListener() {
        super(PacketEventPriority.NORMAL);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PacketProcessor.handlePacket(event.getPlayer(), new Packet(event.getNMSPacket(), event.getPacketId()));
    }

    /*public NetworkListener() {
        super(ClientDetector.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CUSTOM_PAYLOAD);
    }*/


    /*@Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
            PacketContainer packet = event.getPacket();

            System.out.println(packet.getMinecraftKeys().read(0).getFullKey());
            System.out.println(packet.get.toString());


        }
    }*/
}
