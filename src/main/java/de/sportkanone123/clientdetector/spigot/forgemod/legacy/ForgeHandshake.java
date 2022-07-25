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

package de.sportkanone123.clientdetector.spigot.forgemod.legacy;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import org.bukkit.entity.Player;

public class ForgeHandshake {


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Client   ║ Discriminator        ║	Byte	      ║ Always 0 for ServerHello
    //          ║          ║ FML protocol Version ║	Byte	      ║ Determined from NetworkRegistery. Currently 2.
    //          ║          ║ Override dimension   ║ Optional Int  ║ Only sent if protocol version is greater than 1.
    public static void sendServerHello(Player player, String channel){
        WrapperPlayServerPluginMessage costumPayload = new WrapperPlayServerPluginMessage(channel,  new byte[] {(byte) 0,(byte) 2,(byte) 0,(byte) 0,(byte) 0,(byte) 0 });
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Both     ║ Discriminator        ║	Byte	      ║ Always 2 for ModList
    //          ║          ║ Number of mods       ║	Varint	      ║ Number of mods below
    //          ║          ║ Mods (name+version)  ║	List(Str,Str) ║
    public static void sendModList(Player player, String channel){
        WrapperPlayServerPluginMessage costumPayload = new WrapperPlayServerPluginMessage(channel,  new byte[] {(byte) 2,(byte) 0,(byte) 0,(byte) 0,(byte) 0 });
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Both     ║ Discriminator        ║	Byte	      ║ Always -1 (255) for HandshakeAck
    //          ║          ║ Phase                ║	Byte	      ║ The current phase, which is the ordinal (0-indexed) in the FMLHandshakeClientState or FMLHandshakeServerState enums (if the server is sending it, it is in the ServerState enum, and if the client is sending it, it is the ClientState enum).
    public static void sendHandshakeAck(Player player, Byte phase, String channel){
        WrapperPlayServerPluginMessage costumPayload = new WrapperPlayServerPluginMessage(channel,  new byte[] {(byte) -1, phase});
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
    }


    //  Channel ║ Bound To ║ Field Name           ║ Field Type    ║	Notes
    //══════════╬══════════╬══════════════════════╬═══════════════╬════════════
    // FML|HS	║ Client   ║ Discriminator        ║	Byte	      ║ Always -2 (254) for HandshakeReset
    public static void sendHandshakeReset(Player player, String channel){
        WrapperPlayServerPluginMessage costumPayload = new WrapperPlayServerPluginMessage(channel,  new byte[] {(byte) -2,(byte) 0 });
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
    }

    public static void sendHandshake(Player player){
        if(PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2) && (PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isNewerThanOrEquals(ClientVersion.V_1_8) && PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isOlderThanOrEquals(ClientVersion.V_1_12_2))){
            sendHandshakeReset(player, "FML|HS");
            sendServerHello(player, "FML|HS");
            sendModList(player, "FML|HS");
        }
    }
}