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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.login.out.custompayload.WrappedPacketLoginOutCustomPayload;

public class ForgeHandshake {
    public static void sendModList(Object channel){

        WrappedPacketLoginOutCustomPayload wrappedPacketLoginOutCustomPayload = new WrappedPacketLoginOutCustomPayload(/*Random ID*/111111, "fml:handshake", new byte[] { 1, 0, 0, 0 });
        PacketEvents.get().getPlayerUtils().sendPacket(channel, wrappedPacketLoginOutCustomPayload);

    }
}
