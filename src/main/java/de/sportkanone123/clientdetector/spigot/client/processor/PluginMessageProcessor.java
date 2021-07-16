package de.sportkanone123.clientdetector.spigot.client.processor;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.client.Client;
import de.sportkanone123.clientdetector.spigot.manager.AlertsManager;
import org.bukkit.entity.Player;

public class PluginMessageProcessor {
    public static void handlePluginMessage(Player player, String channel, byte[] data){
        for(Client client : ClientDetector.CLIENTS){
            if(client.isClient(channel, data) && ClientDetector.plugin.getConfig().getBoolean("client.enableClientDetection")){
                if(client.getClientName() == "Vanilla Minecraft / Undetectable Client"){
                    if(ClientDetector.playerClient.get(player) == null){
                        ClientDetector.playerClient.put(player, client.getClientName());

                        if(ClientDetector.mcVersion.get(player) != null && client.getClientName() != null)
                            ClientDetector.clientVersion.put(player, ClientDetector.mcVersion.get(player));

                        //Bukkit.getPluginManager().callEvent(new ClientDetectEvent(player, "Vanilla Minecraft / Undetectable Client"));

                        AlertsManager.handleClientDetection(player);
                    }
                }else{
                    if(ClientDetector.playerClient.get(player) == null || ClientDetector.playerClient.get(player) == "Vanilla Minecraft / Undetectable Client" || ClientDetector.playerClient.get(player) == "Unknown Client (Not Vanilla Minecraft)"){
                        ClientDetector.playerClient.put(player, client.getClientName());

                        if(client.getHasVersion() && client.getVersion(channel, data) != null && ClientDetector.plugin.getConfig().getBoolean("client.enableVersionDetection"))
                            ClientDetector.clientVersion.put(player, client.getVersion(channel, data));
                        else
                            ClientDetector.clientVersion.put(player, null);

                        //Bukkit.getPluginManager().callEvent(new ClientDetectEvent(player, client.getClientName()));

                        AlertsManager.handleClientDetection(player);
                    }
                }
            }
        }
    }
}
