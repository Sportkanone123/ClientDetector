package de.sportkanone123.clientdetector.spigot.forgemod.legacy;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ForgeHandshake {


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Client   ║ Discriminator        ║	Byte	      ║ Always 0 for ServerHello
    //          ║          ║ FML protocol Version ║	Byte	      ║ Determined from NetworkRegistery. Currently 2.
    //          ║          ║ Override dimension   ║ Optional Int  ║ Only sent if protocol version is greater than 1.
    public static void sendServerHello(Player player){
        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("FML|HS",  new byte[] {(byte) 0,(byte) 2,(byte) 0,(byte) 0,(byte) 0,(byte) 0 });
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Both     ║ Discriminator        ║	Byte	      ║ Always 2 for ModList
    //          ║          ║ Number of mods       ║	Varint	      ║ Number of mods below
    //          ║          ║ Mods (name+version)  ║	List(Str,Str) ║
    public static void sendModList(Player player){
        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("FML|HS",  new byte[] {(byte) 2,(byte) 0,(byte) 0,(byte) 0,(byte) 0 });
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Both     ║ Discriminator        ║	Byte	      ║ Always -1 (255) for HandshakeAck
    //          ║          ║ Phase                ║	Byte	      ║ The current phase, which is the ordinal (0-indexed) in the FMLHandshakeClientState or FMLHandshakeServerState enums (if the server is sending it, it is in the ServerState enum, and if the client is sending it, it is the ClientState enum).
    public static void sendHandshakeAck(Player player, Byte phase){
        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("FML|HS",  new byte[] {(byte) -1,(byte) phase });
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Client   ║ Discriminator        ║	Byte	      ║ Always -2 (254) for HandshakeReset
    public static void sendHandshakeReset(Player player){
        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("FML|HS",  new byte[] {(byte) -2,(byte) 0 });
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }

    public static void sendHandshake(Player player){
        if(PacketEvents.get().getPlayerUtils().getClientVersion(player).isOlderThanOrEquals(ClientVersion.v_1_12_2)){
            sendHandshakeReset(player);
            sendServerHello(player);
            sendModList(player);
        }
    }
}
