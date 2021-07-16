package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderManager extends PlaceholderExpansion {
    static Plugin plugin = ClientDetector.plugin;

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "clientdetector";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if(identifier.equalsIgnoreCase("client_name")){
            if(ClientDetector.playerClient.get(player) != null)
                return ClientDetector.playerClient.get(player);
            return "Vanilla Minecraft / Undetectable Client";
        }
        if(identifier.equalsIgnoreCase("client_version")){
            if(ClientDetector.clientVersion.get(player) != null)
                return ClientDetector.clientVersion.get(player);
            return null;
        }
        if(identifier.equalsIgnoreCase("forge_user")){
            if(ClientDetector.forgeMods.get(player).getMods() == null || ClientDetector.forgeMods.get(player).getMods().isEmpty())
                return "false";
            return "true";
        }
        if(identifier.equalsIgnoreCase("bedrock_player")){
            return String.valueOf(GeyserManager.isBedrockPlayer(player));
        }
        return null;
    }
}
