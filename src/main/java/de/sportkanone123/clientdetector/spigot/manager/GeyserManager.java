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

package de.sportkanone123.clientdetector.spigot.manager;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GeyserManager {
    public static boolean isBedrockPlayer(Player player){
        if(Bukkit.getPluginManager().isPluginEnabled("floodgate-bukkit") || Bukkit.getPluginManager().isPluginEnabled("floodgate-bungee")  || Bukkit.getPluginManager().isPluginEnabled("floodgate-common")){
            //return org.geysermc.floodgate.FloodgateAPI.isBedrockPlayer(player);
        }

        /*if(Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot") || Bukkit.getPluginManager().isPluginEnabled("Geyser-BungeeCord")  || Bukkit.getPluginManager().isPluginEnabled("Geyser-Sponge") || Bukkit.getPluginManager().isPluginEnabled("Geyser")){
            org.geysermc.connector.network.session.GeyserSession session = org.geysermc.connector.GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId());

            if(session == null)
                return false;
            else
                return true;
        }*/

        return PacketEvents.get().getPlayerUtils().isGeyserPlayer(player.getPlayer());
    }
}
