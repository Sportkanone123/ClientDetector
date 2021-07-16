package de.sportkanone123.clientdetector.spigot.command.impl;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class Player {
    public static boolean handle(CommandSender sender, Command command, String label, String[] args) {
        org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;

        if(args.length >= 3){
            if(Bukkit.getPlayer(args[2]) != null){
                org.bukkit.entity.Player target = Bukkit.getPlayer(args[2]);
                if(args[1].equalsIgnoreCase("client")){
                    if(ClientDetector.playerClient.get(target) != null && ClientDetector.clientVersion.get(target) == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithoutversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", ClientDetector.playerClient.get(target))));
                    }else if(ClientDetector.playerClient.get(target) != null && ClientDetector.clientVersion.get(target) != null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", ClientDetector.playerClient.get(target)).replace("%client_version%", ClientDetector.clientVersion.get(target))));
                    }else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("player.clientwithoutversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%player_uuid%", target.getUniqueId().toString()).replace("%client_name%", "Vanilla Minecraft / Undetectable Client")));
                    }
                }else if(args[1].equalsIgnoreCase("mods")){
                    if(ConfigManager.getConfig("message").getString("player.playermods") != null){
                        ArrayList<String> message =  (ArrayList<String>) ConfigManager.getConfig("message").get("player.playermods");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(0).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        if(ClientDetector.playerMods.get(target) != null){
                            for(String mod : ClientDetector.playerMods.get(target)){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(1).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%mod_name%", mod)));
                            }
                        }
                    }
                }else if(args[1].equalsIgnoreCase("version")){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , ConfigManager.getConfig("message").getString("player.mcversion").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName()).replace("%mc_version%", PacketEvents.get().getPlayerUtils().getClientVersion(target).name())));
                }else if(args[1].equalsIgnoreCase("labyaddons")){
                    if(ConfigManager.getConfig("message").getString("player.playerlabymods") != null){
                        ArrayList<String> message =  (ArrayList<String>) ConfigManager.getConfig("message").get("player.playermods");

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(0).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", target.getName())));
                        if(ClientDetector.playerLabymodMods.get(target) != null){
                            for(String mod : ClientDetector.playerLabymodMods.get(target)){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.get(1).replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%mod_name%", mod)));
                            }
                        }
                    }
                }
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("error.playernotfound").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", args[2])));
            }
        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("prefix") + " &cPlease use: /cd player <client/version/mods/labyaddons> <player>"));
        }
        return false;
    }
}
