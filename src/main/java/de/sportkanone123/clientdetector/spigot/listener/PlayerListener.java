package de.sportkanone123.clientdetector.spigot.listener;

import de.sportkanone123.clientdetector.bungeecord.utils.CustomPayload;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.clientdisabler.ClientDisabler;
import de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandshake;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import de.sportkanone123.clientdetector.spigot.manager.GeyserManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event){
        ClientDetector.playerMods.put(event.getPlayer(), new ArrayList<String>());
        ClientDetector.clientVersion.put(event.getPlayer(), null);
        ClientDetector.playerMods.put(event.getPlayer(), null);
        ClientDetector.playerLabymodMods.put(event.getPlayer(), new ArrayList<String>());

        if(ClientDetector.plugin.getConfig().getBoolean("forge.simulateForgeHandshake"))
            ForgeHandshake.sendHandshake(event.getPlayer());

        if(GeyserManager.isBedrockPlayer(event.getPlayer()))
            AlertsManager.handleGeyserDetection(event.getPlayer());

        /* Needed for Cracked Vape detection */
        event.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8 ");

        if(ClientDetector.plugin.getConfig().getBoolean("client.enableMinecraftVersionDetection")){
            ClientDetector.mcVersion.put(event.getPlayer(), PacketEvents.get().getPlayerUtils().getClientVersion(event.getPlayer()).toString());
        }

        if(ClientDetector.bungeePayload.get(event.getPlayer().getUniqueId()) != null){
            for(CustomPayload customPayload : ClientDetector.bungeePayload.get(event.getPlayer().getUniqueId())){
                de.sportkanone123.clientdetector.spigot.client.processor.PacketProcessor.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());
                de.sportkanone123.clientdetector.spigot.mod.processor.PacketProcessor.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());

                de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());

                de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePacket(Bukkit.getPlayer(customPayload.getUuid()), customPayload.getChannel(), customPayload.getData());
            }
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event){

        ClientDetector.bungeePayload.put(event.getPlayer().getUniqueId(), new ArrayList<CustomPayload>());
    }

    /*@EventHandler
    public static void onChat(AsyncPlayerChatEvent event){
        ClientDisabler.handleChatEvent(event);
    }*/
}
