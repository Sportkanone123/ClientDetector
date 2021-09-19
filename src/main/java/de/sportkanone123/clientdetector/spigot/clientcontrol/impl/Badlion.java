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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/*
More information can be found here: https://github.com/LunarClient/BukkitAPI
 */
public class Badlion {
    private static final Map<String, DisallowedMods> modsDisallowed =  new HashMap<String, DisallowedMods>();

    public static void handle(Player player){
        Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();

        /*
        · Animations
        · ArmorStatus
        · AutoFriend
        · AutoGG
        · AutoText
        · AutoTip
        · Block Overlay
        · BlockInfo
        · BossBar
        · Chat
        · Chunk Borders
        · Clear Glass
        · ClearWater
        · Clock
        · Combo Counter
        · Coordinates
        · CPS
        · Crosshair
        · Custom Fonts
        · Direction
        · EnchantGlint
        · FOV Changer
        · FPS
        · Fullbright
        · Hit Color
        · Hitboxes
        · InventoryBlur
        · Item Counter
        · Item Info
        · Item Physics
        · Item Tracker
        · JustEnoughItems
        · Keystrokes
        · LevelHead
        · Light Overlay
        · Memory
        · MiniMap
        · MLG Cobweb
        · MotionBlur
        · MumbleLink
        · Music
        · Name History
        · NickHider
        · NotEnoughUpdates
        · Notifications
        · Pack Display
        · Pack Tweaks
        · Particles
        · Perspective
        · Ping
        · Player Counter
        · PotionStatus
        · Protection
        · Quickplay
        · Reach Display
        · Replay
        · Saturation
        · Schematica
        · Scoreboard
        · Server Address
        · Shinypots
        · SkyblockAddons
        · Stopwatch
        · TcpNoDelay
        · TeamSpeak
        · TimeChanger
        · Timers
        · ToggleChat
        · ToggleSneak
        · ToggleSprint
        · Waypoints
        · WeatherChanger
        · World Edit CUI
        · Zoom
         */

        for(String string : ((MemorySection) ConfigManager.getConfig("clientcontrol").get("badlion_disable")).getKeys(false)){
            if(string.equalsIgnoreCase("ToggleSprint")){
                if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string + ".disableAll")) {
                    modsDisallowed.put(string, new DisallowedMods(true, null, null));
                }else if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string + ".FlySpeed")){
                    JsonObject object = new JsonObject();
                    JsonObject object2 = new JsonObject();
                    object2.addProperty("disabled", true);
                    object.add("flySpeed", object2);
                    modsDisallowed.put(string, new DisallowedMods(false, object, null));
                }
            }

            if(string.equalsIgnoreCase("ToggleSneak")){
                if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string + ".disableAll")) {
                    modsDisallowed.put(string, new DisallowedMods(true, null, null));
                }else if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string + ".InventorySneak")){
                    JsonObject object = new JsonObject();
                    JsonObject object2 = new JsonObject();
                    object2.addProperty("disabled", true);
                    object.add("inventorySneak", object2);
                    modsDisallowed.put(string, new DisallowedMods(false, object, null));
                }
            }

            if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string))
                modsDisallowed.put(string, new DisallowedMods(true, null, null));
        }


        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("badlion:mods",  GSON.toJson(modsDisallowed).getBytes());
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }

    private static class DisallowedMods {
        private final boolean disabled;
        private final JsonObject extra_data;
        private final JsonObject settings;

        public DisallowedMods(boolean disabled, JsonObject extra_data, JsonObject settings){
            this.disabled = disabled;
            this.extra_data = extra_data;
            this.settings = settings;
        }
    }
}
