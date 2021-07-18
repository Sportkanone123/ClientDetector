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
