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

package de.sportkanone123.clientdetector.spigot.api;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.GeyserManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetectorAPI {
    public static String getPlayerClient(Player player){
        if(ClientDetector.playerClient.get(player.getUniqueId()) != null)
            return ClientDetector.playerClient.get(player.getUniqueId());
        return "Vanilla Minecraft / Undetectable Client";
    }

    public static List<String> getPlayerMods(Player player){
        if(ClientDetector.playerMods.get(player.getUniqueId()) != null)
            return ClientDetector.playerMods.get(player.getUniqueId());
        return new ArrayList<String>();
    }

    public static ArrayList<String> getPlayerForgeMods(Player player){
        if(ClientDetector.forgeMods.get(player.getUniqueId()) != null)
            return ClientDetector.forgeMods.get(player.getUniqueId()).getMods();
        return new ArrayList<String>();
    }

    public static List<String> getPlayerLabymodAddons(Player player){
        if(ClientDetector.playerLabymodMods.get(player.getUniqueId()) != null)
            return ClientDetector.playerLabymodMods.get(player.getUniqueId());
        return new ArrayList<String>();
    }

    public static Boolean isForgePlayer(Player player){
        return ClientDetector.forgeMods != null && ClientDetector.forgeMods.get(player.getUniqueId()) != null && ClientDetector.forgeMods.get(player.getUniqueId()).getMods() != null && !ClientDetector.forgeMods.get(player.getUniqueId()).getMods().isEmpty();
    }

    public static Boolean isBedrockPlayer(Player player){
        return GeyserManager.isBedrockPlayer(player);
    }
}
