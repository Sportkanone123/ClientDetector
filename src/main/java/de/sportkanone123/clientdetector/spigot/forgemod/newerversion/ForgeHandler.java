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

package de.sportkanone123.clientdetector.spigot.forgemod.newerversion;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.api.events.ForgeModlistDetectedEvent;
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import io.github.retrooper.packetevents.event.impl.PacketLoginReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.login.in.custompayload.WrappedPacketLoginInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.in.start.WrappedPacketLoginInStart;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ForgeHandler {
    static HashMap<Object, String> channelToName = new HashMap<>();
    static HashMap<String, ModList> nameToModlist = new HashMap<>();


    public static void handle(PacketLoginReceiveEvent event){
        if(event.getPacketId() == PacketType.Login.Client.START) {
            if(ClientDetector.plugin.getConfig().getBoolean("forge.simulateForgeHandshake")) {
                ForgeHandshake.sendModList(event.getChannel());
                channelToName.put(event.getChannel(), new WrappedPacketLoginInStart(event.getNMSPacket()).getGameProfile().getName());
            }

        }else if(event.getPacketId() == PacketType.Login.Client.CUSTOM_PAYLOAD) {
            nameToModlist.put(channelToName.get(event.getChannel()), getModList(new WrappedPacketLoginInCustomPayload(event.getNMSPacket()).getData()));
            event.setCancelled(true);
        }
    }

    public static void handleJoin(Player player){
        if(nameToModlist.containsKey(player.getName())){
            ClientDetector.forgeMods.put(player, nameToModlist.get(player.getName()));

            for(String forgeMod : nameToModlist.get(player.getName()).getMods())
                de.sportkanone123.clientdetector.spigot.forgemod.ForgeHandler.handleDetection(player, forgeMod);

            Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable(){
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new ForgeModlistDetectedEvent(player, nameToModlist.get(player.getName())));
                }
            });
        }
    }

    public static ModList getModList(byte[] data){
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(data);

        ArrayList<String> modList = new ArrayList<>();

        if (getInt(wrappedBuffer) != 2) {
            return new ModList(modList);
        }

        for (int i = getInt(wrappedBuffer), i2 = 0; i2 < i; ++i2) {
            modList.add(getString(wrappedBuffer));
        }

        return new ModList(modList);
    }

    public static String getString(final ByteBuf byteBuf) {
        int i = getInt(byteBuf);
        int i2;
        if (i > (i2 = 256) * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + i2 * 4 + ")");
        }
        if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        String string = byteBuf.toString(byteBuf.readerIndex(), i, StandardCharsets.UTF_8);
        byteBuf.readerIndex(byteBuf.readerIndex() + i);
        if (string.length() > i2) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + i2 + ")");
        }
        return string;
    }

    public static int getInt(final ByteBuf byteBuf) {
        int i = 0;
        int i2 = 0;
        byte byte1;
        do {
            byte1 = byteBuf.readByte();
            i |= (byte1 & 0x7F) << i2++ * 7;
            if (i2 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((byte1 & 0x80) == 0x80);
        return i;
    }
}
