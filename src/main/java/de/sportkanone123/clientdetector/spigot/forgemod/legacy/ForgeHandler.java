package de.sportkanone123.clientdetector.spigot.forgemod.legacy;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.forgemod.ModList;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ForgeHandler {
    public static void handle(Player player, String channel, byte[] data){
        if(ClientDetector.plugin.getConfig().getBoolean("forge.enableLegacyDetection")){
            if(channel.equalsIgnoreCase("FML|HS") && data != null && data[0] == 2){
                ClientDetector.forgeMods.put(player, getModList(data));

                for(String forgeMod : getModList(data).getMods().keySet())
                    de.sportkanone123.clientdetector.spigot.forgemod.ForgeHandler.handleDetection(player, forgeMod);
            }
        }
    }

    public static ModList getModList(byte[] data){
        Map<String, String> modList = new HashMap<>();

        boolean modname = false;
        String tempName = null;

        for (int i = 2; i < data.length; modname = !modname)
        {
            int i2 = i + data[i] + 1;
            byte[] range = Arrays.copyOfRange(data, i + 1, i2);

            String string = new String(range);

            if (modname)
            {
                modList.put(tempName, string);
            }
            else
            {
                tempName = string;
            }

            i = i2;
        }

        return new ModList(modList);
    }
}
