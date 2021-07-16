package de.sportkanone123.clientdetector.spigot.forgemod;

import java.util.HashMap;
import java.util.Map;

public class ModList {
    private Map<String, String> mods = new HashMap<>();

    public ModList(Map<String, String> mods){
        this.mods = mods;
    }

    public Map<String, String> getMods() {
        return mods;
    }

    public void setMods(HashMap<String, String> mods) {
        this.mods = mods;
    }
}
