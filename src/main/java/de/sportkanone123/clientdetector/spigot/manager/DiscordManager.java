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

import de.sportkanone123.clientdetector.spigot.discord.DiscordWebhook;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DiscordManager {
    private static URL url;

    public static boolean load(){
        if(ConfigManager.getConfig("config").get("discord.webHookUrl") != null && !ConfigManager.getConfig("config").get("discord.webHookUrl").equals("") && !ConfigManager.getConfig("config").get("discord.webHookUrl").equals(" ")){
            try {
                url = new URL(ConfigManager.getConfig("config").getString("discord.webHookUrl"));
                return true;
            } catch (MalformedURLException e) {
                return false;
            }
        }else {
            return false;
        }
    }

    public static void handle(Player player, String clientName){
        if(url != null){
            DiscordWebhook webhook = new DiscordWebhook(url.toString());
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle(ConfigManager.getConfig("config").getString("discord.embedTitle").replace("%player_name%", player.getName()).replace("%player_uuid%", player.getUniqueId().toString()).replace("%client_name%", clientName))
                    .setDescription(ConfigManager.getConfig("config").getString("discord.embedMessage").replace("%player_name%", player.getName()).replace("%player_uuid%", player.getUniqueId().toString()).replace("%client_name%", clientName))
                    .setColor(Color.LIGHT_GRAY)
                    .setThumbnail("https://crafatar.com/avatars/" + player.getUniqueId())
                    .setFooter("Check out ClientDetctor on SpigotMC: https://www.spigotmc.org/resources/clientdetector.90375/", "https://www.spigotmc.org/data/resource_icons/90/90375.jpg?1616258526"));

            try {
                webhook.execute(); //Handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
