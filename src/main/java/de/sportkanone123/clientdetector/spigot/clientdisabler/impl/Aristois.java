package de.sportkanone123.clientdetector.spigot.clientdisabler.impl;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.clientdisabler.ClientDisabler;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Aristois {
    /*static HashMap<Player, Boolean> clickedText = new HashMap<>();
    static String randomString = UUID.randomUUID().toString();

    public static void handle(CommandSender sender, Player target){
        clickedText.put(target, false);

        new BukkitRunnable() {
            public void run() {
                if(!clickedText.get(target)){
                    if(ConfigManager.getConfig("message").get("clientdisabler.multiple_messages.clickable_text_step1") != null && ConfigManager.getConfig("message").get("clientdisabler.multiple_messages.clickable_text_step2") != null){
                        ArrayList<String> list = (ArrayList<String>) ConfigManager.getConfig("message").get("clientdisabler.multiple_messages.clickable_text_step1");

                        for(String str : list){
                            ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", ConfigManager.getConfig("message").getString("prefix"))));
                            componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".unload"));

                            target.spigot().sendMessage(componentBuilder.create());
                        }

                        ArrayList<String> list2 = (ArrayList<String>) ConfigManager.getConfig("message").get("clientdisabler.multiple_messages.clickable_text_step2");

                        for(String str : list2){
                            ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", ConfigManager.getConfig("message").getString("prefix"))));
                            componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "#" + randomString));

                            target.spigot().sendMessage(componentBuilder.create());
                        }
                    }
                }else{
                    clickedText.put(target, true);
                    this.cancel();
                }
            }
        }.runTaskTimer(ClientDetector.plugin, 0L, 100L);
    }

    public static void handleChat(AsyncPlayerChatEvent event){
        if(clickedText.get(event.getPlayer()) != null){
            if(event.getMessage().equalsIgnoreCase(".unload") || event.getMessage().equalsIgnoreCase("#" + randomString)){
                event.setCancelled(true);
                event.setMessage(" ");
                if(event.getMessage().equalsIgnoreCase("#" + randomString)){
                    new BukkitRunnable() {
                        public void run() {
                            clickedText.put(event.getPlayer(), true);
                            Bukkit.broadcastMessage("Successful!");
                        }
                    }.runTask(ClientDetector.plugin);
                }
            }
        }
    }*/
}
