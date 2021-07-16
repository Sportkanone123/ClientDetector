package de.sportkanone123.clientdetector.spigot.clientcontrol;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.clientcontrol.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClientControl implements Listener {

    @EventHandler()
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getScheduler().runTaskLater(ClientDetector.plugin, () -> {
            LunarClient.handle(event.getPlayer());
        }, 60L);

        Badlion.handle(event.getPlayer());
        FiveZig.handle(event.getPlayer());
        LabyMod.hande(event.getPlayer());
    }

    public static void handlePacket(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("wdl:control") || channel.equalsIgnoreCase("WDL|INIT") || (channel.equalsIgnoreCase("REGISTER") && new String(data).contains("WDL|INIT")))
            WorldDownloader.handle(player);

        if(channel.equalsIgnoreCase("bsm:settings"))
            BetterSprinting.handle(player);

        if(channel.equalsIgnoreCase("LABYMOD") || channel.equalsIgnoreCase("labymod3:main")){
            LabyMod.handlePacket(player, data);
        }
    }

    public static void handlePluginMessage(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("wdl:control") || channel.equalsIgnoreCase("WDL|INIT") || (channel.equalsIgnoreCase("REGISTER") && new String(data).contains("WDL|INIT")))
            WorldDownloader.handle(player);

        if(channel.equalsIgnoreCase("bsm:settings"))
            BetterSprinting.handle(player);

        if(channel.equalsIgnoreCase("LABYMOD") || channel.equalsIgnoreCase("labymod3:main")){
            LabyMod.handlePluginMessage(player, data);
        }
    }
}
