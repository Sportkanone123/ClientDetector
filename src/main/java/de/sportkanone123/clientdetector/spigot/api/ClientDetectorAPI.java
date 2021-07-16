package de.sportkanone123.clientdetector.spigot.api;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.GeyserManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetectorAPI {
    public static String getPlayerClient(Player player){
        if(ClientDetector.playerClient.get(player) != null)
            return ClientDetector.playerClient.get(player);
        return "Vanilla Minecraft / Undetectable Client";
    }

    public static List<String> getPlayerMods(Player player){
        if(ClientDetector.playerMods.get(player) != null)
            return ClientDetector.playerMods.get(player);
        return new ArrayList<String>();
    }

    public static Map<String, String> getPlayerForgeMods(Player player){
        if(ClientDetector.forgeMods.get(player) != null)
            return ClientDetector.forgeMods.get(player).getMods();
        return new HashMap<String, String>();
    }

    public static List<String> getPlayerLabymodAddons(Player player){
        if(ClientDetector.playerLabymodMods.get(player) != null)
            return ClientDetector.playerLabymodMods.get(player);
        return new ArrayList<String>();
    }

    public static Boolean isForgePlayer(Player player){
        if(ClientDetector.forgeMods.get(player).getMods() == null || ClientDetector.forgeMods.get(player).getMods().isEmpty())
            return false;
        return true;
    }

    public static Boolean isBedrockPlayer(Player player){
        return GeyserManager.isBedrockPlayer(player);
    }
}
