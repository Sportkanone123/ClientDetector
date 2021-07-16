package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.bungeecord.utils.CustomPayload;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class BungeeManager {
    public static void handle(Player player, String channel, byte[] data){
        if(channel.equalsIgnoreCase("clientdetector:fix")){
            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(data));

            String uuid = "";
            String channelMessage = "";
            String dataMessage = "";

            try {
                uuid = msgin.readUTF();
                channelMessage = msgin.readUTF();
                dataMessage = msgin.readUTF();
            } catch (IOException e) {
                return;
            }

            /*System.out.println("-----------[Packet]-----------");
            System.out.println("Player: " + uuid);
            System.out.println("Channel: '" + channelMessage + "'");
            System.out.println("Data: '" + dataMessage + "'");
            System.out.println("-----------[Packet]-----------");*/

            if(ClientDetector.bungeePayload.get(UUID.fromString(uuid)) == null)
                ClientDetector.bungeePayload.put(UUID.fromString(uuid) , new ArrayList<CustomPayload>());

            ClientDetector.bungeePayload.get(UUID.fromString(uuid)).add(new CustomPayload(UUID.fromString(uuid), channelMessage, dataMessage.getBytes(StandardCharsets.UTF_8)));
        }
    }
}
