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

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.bungee.DataType;
import de.sportkanone123.clientdetector.spigot.mod.Mod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ModManager {

    public static void load(){
        ClientDetector.MODS.add(new Mod(Arrays.asList("5zig_Set", "l:5zig_set", "the5zigmod:5zig_set"), Arrays.asList(""), "5zig Mod", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("BSprint", "BSM", "l:bsprint", "l:bsm"), Arrays.asList(""), "Better Sprinting Mod", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("WDL|INIT", "WDL|CONTROL", "wdl:request", "wdl:init", "wdl:control"), Arrays.asList(""), "World Downloader", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("journeymap_channel", "journeymap:channel"), Arrays.asList(""), "JourneyMap", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("WECUI"), Arrays.asList(""), "WorldEditCUI", true));
    }

    public static void unLoad(){
        ClientDetector.MODS = new ArrayList<Mod>();
    }

    public static void handleDetection(Player player, String mod){
        if(ClientDetector.bungeeManager != null && ConfigManager.getConfig("config").getBoolean("bungee.enableBungeeClient"))
            ClientDetector.bungeeManager.syncList(DataType.MOD_LIST, player, ClientDetector.playerMods.get(player.getUniqueId()));

        if(ClientDetector.plugin.getConfig().getBoolean("mods.enableWhitelist")){
            if(ClientDetector.plugin.getConfig().get("mods.whitelistedMods") != null) {
                List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("mods.whitelistedMods");
                if ((!whitelist.contains(mod) && !whitelist.contains(mod.toLowerCase(Locale.ROOT))) && !player.hasPermission("clientdetector.bypass")) {
                    Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("mods.punishCommandWhitelist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                        }
                    });
                }
            }
        }

        if(ClientDetector.plugin.getConfig().getBoolean("mods.enableBlacklist")){
            if(ClientDetector.plugin.getConfig().get("mods.blacklistedMods") != null){
                List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("mods.blacklistedMods");
                if((blacklist.contains(mod) || blacklist.contains(mod.toLowerCase(Locale.ROOT)))  && !player.hasPermission("clientdetector.bypass")){
                    Bukkit.getScheduler().runTask(ClientDetector.plugin, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("mods.punishCommandBlacklist").replace("%player_name%", player.getName()).replace("%mod_name%", mod).replace("%player_uuid%", player.getUniqueId().toString()));
                        }
                    });
                }
            }
        }
    }
}
