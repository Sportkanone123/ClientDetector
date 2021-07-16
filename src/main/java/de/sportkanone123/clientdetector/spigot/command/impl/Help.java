package de.sportkanone123.clientdetector.spigot.command.impl;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help {

    public static boolean handle(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        String prefix = ConfigManager.getConfig("message").getString("prefix");
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l-------&7" + prefix + "&7&7&l-------&7"));
        send_raw(p, "         ┌──────────┐", "cd help");
        send_raw(p, "         │          Help        │", "cd help");
        send_raw(p, "         └──────────┘", "cd help");
        send_raw(p, "         ┌──────────┐", "cd player");
        send_raw(p, "         │        Player       │", "cd player");
        send_raw(p, "         └──────────┘", "cd player");
        send_raw(p, "         ┌──────────┐", "cd forge");
        send_raw(p, "         │        Forge        │", "cd forge");
        send_raw(p, "         └──────────┘", "cd forge");
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l-------&7" + prefix + "&7&7&l-------&7"));

        return false;
    }

    public static void send_raw(Player player, String text, String command){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " {\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command + "\"}}]");
    }
}
