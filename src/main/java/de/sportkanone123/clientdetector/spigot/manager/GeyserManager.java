package de.sportkanone123.clientdetector.spigot.manager;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GeyserManager {
    public static boolean isBedrockPlayer(Player player){
        if(Bukkit.getPluginManager().isPluginEnabled("floodgate-bukkit") || Bukkit.getPluginManager().isPluginEnabled("floodgate-bungee")  || Bukkit.getPluginManager().isPluginEnabled("floodgate-common")){
            //return org.geysermc.floodgate.FloodgateAPI.isBedrockPlayer(player);
        }

        /*if(Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot") || Bukkit.getPluginManager().isPluginEnabled("Geyser-BungeeCord")  || Bukkit.getPluginManager().isPluginEnabled("Geyser-Sponge") || Bukkit.getPluginManager().isPluginEnabled("Geyser")){
            org.geysermc.connector.network.session.GeyserSession session = org.geysermc.connector.GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId());

            if(session == null)
                return false;
            else
                return true;
        }*/

        return PacketEvents.get().getPlayerUtils().isGeyserPlayer(player.getPlayer());
    }
}
