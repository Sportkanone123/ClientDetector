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

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.api.events.ForgeModlistDetectedEvent;
import de.sportkanone123.clientdetector.spigot.bungee.DataType;
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ForgeHandler {
    public static void handle(Player player, String channel, byte[] data){
        if(ConfigManager.getConfig("config").getBoolean("forge.enableLegacyDetection")){
            if(channel.equalsIgnoreCase("FML|HS") && data != null && data[0] == 2){
                ClientDetector.forgeMods.put(player.getUniqueId(), getModList(data));

                if(ClientDetector.bungeeManager != null && ConfigManager.getConfig("config").getBoolean("bungee.enableBungeeClient"))
                    ClientDetector.bungeeManager.syncList(DataType.FORGE_MOD_LIST, player, ClientDetector.forgeMods.get(player.getUniqueId()).getMods());

                for(String forgeMod : getModList(data).getMods())
                    de.sportkanone123.clientdetector.spigot.forgemod.ForgeHandler.handleDetection(player, forgeMod);

                Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable(){
                    @Override
                    public void run() {
                        Bukkit.getPluginManager().callEvent(new ForgeModlistDetectedEvent(player, getModList(data)));
                    }
                });
            }
        }
    }

    public static ModList getModList(byte[] data){
        ArrayList<String> modList = new ArrayList<>();

        boolean modname = false;
        String tempName = null;

        for (int i = 2; i < data.length; modname = !modname)
        {
            int i2 = i + data[i] + 1;
            byte[] range = Arrays.copyOfRange(data, i + 1, i2);

            String string = new String(range);

            if (modname)
            {
                modList.add(tempName);
            }
            else
            {
                tempName = string;
            }

            i = i2;
        }

        return new ModList(modList);
    }
}
