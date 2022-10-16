package de.sportkanone123.clientdetector.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@com.velocitypowered.api.plugin.Plugin(
        id = "clientdetector",
        name = "ClientDetector",
        version = "2.5.2",
        description = "A simple plugin to detect and manage a player's client/mods",
        authors = {"Sportkanone123"}
)
public class ClientDetectorVelocity {

    public static final LegacyChannelIdentifier LEGACY_BUNGEE_CHANNEL = new LegacyChannelIdentifier("cd:bungee");
    public static final MinecraftChannelIdentifier MODERN_BUNGEE_CHANNEL = MinecraftChannelIdentifier.create("cd", "bungee");
    public static final MinecraftChannelIdentifier MODERN_SPIGOT_CHANNEL = MinecraftChannelIdentifier.create("cd", "spigot");

    private ProxyServer server;
    private Logger logger;
    private Path dataDirectory;
    private Map<RegisteredServer, List<byte[]>> queue = new HashMap<>();

    @Inject
    public ClientDetectorVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        //this.server.getEventManager().register(this, this); (Velocity already auto registered the main class)
        this.server.getChannelRegistrar().register(LEGACY_BUNGEE_CHANNEL, MODERN_BUNGEE_CHANNEL);

        runQueue();
    }

    public void runQueue() {
        this.server.getScheduler().buildTask(this, new Runnable() {
            @Override
            public void run() {
                for(RegisteredServer registeredServer : queue.keySet()){
                    if(!registeredServer.getPlayersConnected().isEmpty() && queue.get(registeredServer) != null){
                        List<byte[]> toRemove = new ArrayList<>();
                        for(byte[] data : queue.get(registeredServer)){
                            toRemove.add(data);
                            registeredServer.sendPluginMessage(MODERN_SPIGOT_CHANNEL, data);
                        }
                        queue.get(registeredServer).removeAll(toRemove);
                    }
                }
            }
        }).repeat(1, TimeUnit.SECONDS);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        System.out.println(event.getIdentifier() + " // " + new java.lang.String(event.getData()));
        if (event.getIdentifier().getId().equalsIgnoreCase("cd:bungee")) {
            sync(event.getData());
        }
    }

    private void sync(String string){
        sync(string.getBytes(StandardCharsets.UTF_8));
    }

    private void sync(byte[] data){
        for(RegisteredServer server : this.server.getAllServers()){
            if(server.getPlayersConnected().isEmpty()){
                if(queue.get(server) == null) queue.put(server, new ArrayList<>());

                if(!new String(data, StandardCharsets.UTF_8).contains("CROSS_SERVER_MESSAGE"))
                    queue.get(server).add(data);
            }else{
                server.sendPluginMessage(MODERN_SPIGOT_CHANNEL, data);
            }
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent e) {
        String placeholder = "@@";
        sync("PLAYER_LEFT" + placeholder + e.getPlayer().getUsername());
    }

}
