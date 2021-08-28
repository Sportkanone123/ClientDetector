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

package de.sportkanone123.clientdetector.spigot.clientcontrol;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.clientcontrol.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

public class ClientControl implements Listener {

    @EventHandler()
    public void onJoin(PlayerJoinEvent event){
        Badlion.handle(event.getPlayer());
        FiveZig.handle(event.getPlayer());
        LabyMod.hande(event.getPlayer());

    }

    @EventHandler
    public void onRegister(final PlayerRegisterChannelEvent event) {
        if (!event.getChannel().equalsIgnoreCase("lunarclient:pm")) {
            return;
        }
        LunarClient.handle(event.getPlayer());
    }

    public static void handlePacket(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("wdl:control") || channel.equalsIgnoreCase("WDL|INIT") || (channel.equalsIgnoreCase("REGISTER") && new String(data).contains("WDL|INIT")))
            WorldDownloader.handle(player);

        if(channel.equalsIgnoreCase("bsm:settings"))
            BetterSprinting.handle(player);

        if(channel.equalsIgnoreCase("LABYMOD") || channel.equalsIgnoreCase("labymod3:main")){
            LabyMod.handlePacket(player, data);
        }
    }

    public static void handlePluginMessage(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("wdl:control") || channel.equalsIgnoreCase("WDL|INIT") || (channel.equalsIgnoreCase("REGISTER") && new String(data).contains("WDL|INIT")))
            WorldDownloader.handle(player);

        if(channel.equalsIgnoreCase("bsm:settings"))
            BetterSprinting.handle(player);

        if(channel.equalsIgnoreCase("LABYMOD") || channel.equalsIgnoreCase("labymod3:main")){
            LabyMod.handlePluginMessage(player, data);
        }
    }
}
