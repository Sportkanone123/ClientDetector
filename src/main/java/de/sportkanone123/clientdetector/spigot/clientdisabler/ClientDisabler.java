package de.sportkanone123.clientdetector.spigot.clientdisabler;

import de.sportkanone123.clientdetector.spigot.clientdisabler.impl.Aristois;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
clientdisabler:
  one_message:
    #Text that will be sent, if a players client is checked. [OPTIONS: "List[anyString]", PLACEHOLDERS: ['%prefix%', '%time_left%'], DEFAULT: [" ", " ", " ", " ", " ", " ", "%prefix% &bPlease click this message to continue playing!", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"]]
    clickable_text:
      - " "
      - " "
      - " "
      - " "
      - " "
      - " "
      - "%prefix% &bPlease click this message to continue playing! "
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
  multiple_messages:
    #Text that will be sent, if a players client is checked. [OPTIONS: "List[anyString]", PLACEHOLDERS: ['%prefix%', '%time_left%'], DEFAULT: [" ", " ", " ", " ", "%prefix% &e\u26a0 &cFirst click the first message and then the second message &e\u26a0", " ", "%prefix% &bPlease click this message to continue playing! &7(&l&bStep 1 &7/ &l&b2&7)", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"]]
    clickable_text_step1:
      - " "
      - " "
      - " "
      - " "
      - "%prefix% &e\u26a0 &cFirst click the first message and then the second message &e\u26a0"
      - " "
      - "%prefix% &bPlease click this message to continue playing! &7(&l&bStep 1 &7/ &l&b2&7)"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
    #Text that will be sent, if a players client is checked. [OPTIONS: "List[anyString]", PLACEHOLDERS: ['%prefix%', '%time_left%'], DEFAULT: [" ", " ", " ", " ", " ", " ", " ", "%prefix% &bPlease click this message to continue playing! &7(&l&bStep 2 &7/ &l&b2&7)", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588", "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"]]
    clickable_text_step2:
      - " "
      - "%prefix% &bPlease click this message to continue playing! &7(&l&bStep 2 &7/ &l&b2&7)"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
      - "%prefix% &b\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
 */

public class ClientDisabler {
    /*public static void checkAristois(CommandSender sender, Player target) {
        Aristois.handle(sender, target);
    }

    public static void handleChatEvent(AsyncPlayerChatEvent event){
        Aristois.handleChat(event);
    }*/
}
