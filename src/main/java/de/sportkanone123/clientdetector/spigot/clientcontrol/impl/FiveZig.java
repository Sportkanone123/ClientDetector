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

package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.entity.Player;

import java.util.BitSet;

public class FiveZig {
    public static void handle(Player player){
        boolean[] features = new boolean[6];

        features[0] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePotionEffectHud");
        features[1] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePotionIndicatorVignette");
        features[2] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableArmourHud");
        features[3] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePlayerSaturation");
        features[4] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableEntityHealthIndicator");
        features[5] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableAutoReconnect");

        final BitSet disableBitSet = new BitSet();

        // Set the according bits
        for (int i = 0; i < features.length; ++i) {
            disableBitSet.set(i, features[i]);
        }

        WrapperPlayServerPluginMessage costumPayload;

        if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)){
            costumPayload = new WrapperPlayServerPluginMessage("the5zigmod:5zig_set",  disableBitSet.toByteArray());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);

            costumPayload = new WrapperPlayServerPluginMessage("l:5zig_set",  disableBitSet.toByteArray());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
        }else{
            costumPayload = new WrapperPlayServerPluginMessage("5zig_Set",  disableBitSet.toByteArray());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, costumPayload);
        }

    }
}
