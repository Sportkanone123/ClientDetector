package de.sportkanone123.clientdetector.spigot.command;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.command.impl.*;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof org.bukkit.entity.Player){
            if(!((org.bukkit.entity.Player) sender).hasPermission("clientdetector.command")){
                String prefix = ConfigManager.getConfig("message").getString("prefix");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------&7" + prefix + "&7&m-------&7"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("prefix") + " &7ClientDetector(" + Bukkit.getServer().getPluginManager().getPlugin("ClientDetector").getDescription().getVersion() + ") by Sportkanone123"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------&7" + prefix + "&7&m-------&7"));

                return false;
            }
        }

        if(args.length == 0){
            String prefix = ConfigManager.getConfig("message").getString("prefix");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------&7" + prefix + "&7&m-------&7"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("prefix") + " &7ClientDetector(" + Bukkit.getServer().getPluginManager().getPlugin("ClientDetector").getDescription().getVersion() + ") by Sportkanone123"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------&7" + prefix + "&7&m-------&7"));
        }else{
            if(args[0].equalsIgnoreCase("help")) {
                Help.handle(sender, command, label, args);
            }else if(args[0].equalsIgnoreCase("player")){
                Player.handle(sender, command, label, args);
            }else if(args[0].equalsIgnoreCase("forge")){
                Forge.handle(sender, command, label, args);
            }
        }

        return false;
    }
}
