package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.mod.Mod;

import java.util.ArrayList;
import java.util.Arrays;

public class ModManager {

    public static void load(){
        ClientDetector.MODS.add(new Mod("REGISTER", "the5zigmod:5zig_set", "5zig Mod", false));
        ClientDetector.MODS.add(new Mod(Arrays.asList("5zig_Set", "l:5zig_set", "the5zigmod:5zig_set"), Arrays.asList(""), "5zig Mod", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("BSprint", "BSM", "l:bsprint", "l:bsm"), Arrays.asList(""), "Better Sprinting Mod", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("WDL|INIT", "WDL|CONTROL", "wdl:request", "wdl:init", "wdl:control"), Arrays.asList(""), "World Downloader", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("journeymap_channel", "journeymap:channel"), Arrays.asList(""), "JourneyMap", true));
        ClientDetector.MODS.add(new Mod(Arrays.asList("WECUI"), Arrays.asList(""), "WorldEditCUI", true));

        /*for(Mod mod : ClientDetector.MODS){
            mod.load();
        }*/
    }



    public static void unLoad(){
        ClientDetector.MODS = new ArrayList<Mod>();
    }
}
