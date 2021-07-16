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

public class Badlion {
    private static Map<String, DisallowedMods> modsDisallowed =  new HashMap<String, DisallowedMods>();

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
            if(ConfigManager.getConfig("clientcontrol").getBoolean("badlion_disable." + string))
                modsDisallowed.put(string, new DisallowedMods(true, null, null));
        }


        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload("badlion:mods",  GSON.toJson(modsDisallowed).getBytes());
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }

    private static class DisallowedMods {
        private boolean disabled;
        private JsonObject extra_data;
        private JsonObject settings;

        public DisallowedMods(boolean disabled, JsonObject extra_data, JsonObject settings){
            this.disabled = disabled;
            this.extra_data = extra_data;
            this.settings = settings;
        }
    }
}
