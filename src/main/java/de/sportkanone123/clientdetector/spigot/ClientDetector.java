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

package de.sportkanone123.clientdetector.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import de.sportkanone123.clientdetector.spigot.bungee.BungeeManager;
import de.sportkanone123.clientdetector.spigot.client.Client;
import de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl;
import de.sportkanone123.clientdetector.spigot.command.Command;
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import de.sportkanone123.clientdetector.spigot.hackdetector.HackDetector;
import de.sportkanone123.clientdetector.spigot.hackdetector.impl.AntiFastMath;
import de.sportkanone123.clientdetector.spigot.listener.NetworkListener;
import de.sportkanone123.clientdetector.spigot.listener.PlayerListener;
import de.sportkanone123.clientdetector.spigot.listener.PluginMessageListener;
import de.sportkanone123.clientdetector.spigot.manager.*;
import de.sportkanone123.clientdetector.spigot.mod.Mod;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class ClientDetector extends JavaPlugin {
    public static Plugin plugin;
    public static ArrayList<Client> CLIENTS = new ArrayList<Client>();
    public static ArrayList<Mod> MODS = new ArrayList<Mod>();

    public static HashMap<UUID, String> mcVersion = new HashMap<UUID, String>();
    public static HashMap<UUID, ModList> forgeMods = new HashMap<UUID, ModList>();
    public static HashMap<UUID, String> playerClient = new HashMap<UUID, String> ();
    public static HashMap<UUID, String> clientVersion = new HashMap<UUID, String> ();
    public static HashMap<UUID, ArrayList<String>> playerMods = new HashMap<UUID, ArrayList<String>> ();
    public static HashMap<UUID, ArrayList<String>> playerLabymodMods = new HashMap<UUID, ArrayList<String>> ();
    public static HashMap<UUID, ArrayList<String>> playerCommandsQueue = new HashMap<UUID, ArrayList<String>>();
    public static BungeeManager bungeeManager;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().readOnlyListeners(false)
                .checkForUpdates(false)
                .bStats(true)
                .debug(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        plugin = this;

        new MetricsManager(this, 10745);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aVersion&7) &aDetected Version &c" + PacketEvents.getAPI().getServerManager().getVersion().name()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aVersion&7) &aLoading settings for Version &c" + PacketEvents.getAPI().getServerManager().getVersion().name()));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aProtocol&7) &aLoading protocols..."));
        PacketEvents.getAPI().getEventManager().registerListener(new NetworkListener());
        PacketEvents.getAPI().init();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "clientdetector:sync");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "lunarclient:pm");

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "clientdetector:sync", new PluginMessageListener());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "clientdetector:fix", new PluginMessageListener());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "lunarclient:pm", new PluginMessageListener());

        if(PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2))
            Bukkit.getMessenger().registerIncomingPluginChannel(this, "CB-Client", new PluginMessageListener());

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aConfig&7) &aLoading config(s)..."));
        saveDefaultConfig();

        getCommand("clientdetector").setExecutor(new Command());
        getCommand("client").setExecutor(new Command());
        getCommand("forge").setExecutor(new Command());
        getCommand("mods").setExecutor(new Command());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClientControl(), this);

        if(ConfigManager.getConfig("config").getBoolean("hackdetector.chatexploit.enableChatExploit") || ConfigManager.getConfig("config").getBoolean("hackdetector.antifastmath.enableAntiFastMath"))
            Bukkit.getPluginManager().registerEvents(new HackDetector(), this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aDetection&7) &aLoading client detections..."));
        ClientManager.load();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aDetection&7) &aLoading mod detections..."));
        ModManager.load();

        AlertsManager.load();

        DiscordManager.load();

        AntiFastMath.load();

        if(ConfigManager.getConfig("config").getBoolean("bungee.enableBungeeClient")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aDetection&7) &aLoading Bungee client..."));

            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "cd:bungee");
            Bukkit.getMessenger().registerIncomingPluginChannel(this, "cd:spigot", new PluginMessageListener());

            bungeeManager = new BungeeManager();
        }

        if(Bukkit.getServer().getPluginManager().isPluginEnabled("ViaVersion")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aViaVersion&7) &aDetected ViaVersion " + Bukkit.getPluginManager().getPlugin("ViaVersion").getDescription().getVersion()));
        }

        if(Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aProtocolLib&7) &aDetected ProtocolLib " + Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion()));
        }

        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aPlaceholderAPI&7) &aDetected PlaceholderAPI " + Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion()));
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && getConfig().getBoolean("placeholder.enablePlaceholder"))
            new PlaceholderManager().register();

        try {
            ConfigManager.loadConfig("message");
            ConfigManager.loadConfig("clientcontrol");

            if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17)){
                if(ConfigManager.optimizeConfig("config", "forge.simulateForgeHandshake", false))
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] &cIMPORTANT NOTIFICATION: &aForge modlist detection for 1.17 - 1.18.2 is currently marked as UNSTABLE and therefore will be automatically disabled!!"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] &aStarted!"));

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();

        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);

        ClientManager.unLoad();
        ModManager.unLoad();
    }
}
