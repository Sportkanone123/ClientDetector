package de.sportkanone123.clientdetector.spigot.forgemod;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.client.Client;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ForgeHandler {
    public static void handleDetection(Player player, String mod){
        if(ClientDetector.plugin.getConfig().getBoolean("forge.blockForge")){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandForge"));
        }

        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableWhitelist")){
            if(ClientDetector.plugin.getConfig().get("forge.whitelistedMods") != null){
                List<String> whitelist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.whitelistedMods");
                if(!whitelist.contains(mod))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandWhitelist"));
            }
        }

        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableBlacklist")){
            if(ClientDetector.plugin.getConfig().get("forge.blacklistedMods") != null){
                List<String> blacklist = (ArrayList<String>) ClientDetector.plugin.getConfig().get("forge.blacklistedMods");
                if(blacklist.contains(mod))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClientDetector.plugin.getConfig().getString("forge.punishCommandBlacklist"));
            }
        }
    }
}
