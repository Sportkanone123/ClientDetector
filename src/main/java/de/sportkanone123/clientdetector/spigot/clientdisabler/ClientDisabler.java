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
