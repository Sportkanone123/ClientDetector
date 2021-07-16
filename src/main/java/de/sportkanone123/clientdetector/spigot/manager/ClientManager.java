package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.client.Client;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientManager {

    public static void load(){
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("FML|HS", "l:fmlhs"), "", "Forge", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("5zig_Set", "l:5zig_set"), "", "5zig Mod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("LABYMOD", "LMC", "labymod3:main"), "", "LabyMod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("PX|Version", "", "PXMod", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("LOLIMAHACKER", "", "Cracked Vape", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("hyperium", "", "Hyperium", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("MC|Pixel", "", "Pixel Client", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("LC|Brand", "", "Winterware", true, false, null, null));
        ClientDetector.CLIENTS.add(new Client("vanilla", "vanilla", "Aristois (Experimental)", false, false, null, null));

        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "vanilla", "Vanilla Minecraft / Undetectable Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "Vanilla", "Vanilla Minecraft / Undetectable Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "Minecraft-Console-Client", "Console Client", false, true, "/", 1));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "LiteLoader", "LiteLoader", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "PLC18", "PvPLounge Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "Subsystem", "Easy Minecraft Client", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "rift", "Rift", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "fabric", "Fabric", false, false, null, null));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), "lunarclient", "Lunar Client", false, true, ":", 1));
        ClientDetector.CLIENTS.add(new Client(Arrays.asList("MC|Brand", "minecraft:brand", "realms"), Arrays.asList("forge", "fml", "fml,forge"), "Forge", false, false, null, null));

        /*for(Client client : ClientDetector.CLIENTS){
            client.load();
        }*/
    }

    public static void unLoad(){
        ClientDetector.CLIENTS = new ArrayList<Client>();
    }
}
