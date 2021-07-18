/*
 * This file is part of ClientDetector - https://github.com/Sportkanone123/ClientDetector
 * Copyright (C) 2021 Sportkanone123
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
