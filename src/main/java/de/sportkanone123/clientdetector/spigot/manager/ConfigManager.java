package de.sportkanone123.clientdetector.spigot.manager;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigManager {
    private static Plugin plugin = ClientDetector.plugin ;
    static HashMap<String , File> files = new HashMap<String , File>();
    static HashMap<String , FileConfiguration> configurations = new HashMap<String , FileConfiguration>();

    public static void loadConfig(String string) throws IOException {
        File file = new File(plugin.getDataFolder(), string + ".yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            plugin.saveResource(string + ".yml", false);
            FileConfiguration configuration = new YamlConfiguration();
            try{
                configuration.load(file);
                files.put(string, file);
                configurations.put(string, configuration);
            } catch(IOException | InvalidConfigurationException e){
                System.out.println("[ClientDetector] Error loading files, please contact the developer and send him this stacktrace:");
                e.printStackTrace();
            }
        }else{
            FileConfiguration configuration = new YamlConfiguration();
            try{
                configuration.load(file);
                files.put(string, file);
                configurations.put(string, configuration);
            } catch(IOException | InvalidConfigurationException e){
                System.out.println("[ClientDetector] Error loading files, please contact the developer and send him this stacktrace:");
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig(String string){
        try {
            if(files.get(string) != null && configurations.get(string) != null){
                configurations.get(string).save(files.get(string));
                configurations.put(string, YamlConfiguration.loadConfiguration(files.get(string)));
            }else{
                loadConfig(string);
                if(files.get(string) != null && configurations.get(string) != null){
                    configurations.get(string).save(files.get(string));
                    configurations.put(string, YamlConfiguration.loadConfiguration(files.get(string)));
                }
            }
        } catch (IOException e) {
            System.out.println("[ClientDetector] Error saving files, please contact the developer and send him this stacktrace:");
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig(String string) {
        if(configurations.get(string) != null){
            return configurations.get(string);
        }else{
            try {
                loadConfig(string);
                if(configurations.get(string) != null){
                    return configurations.get(string);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new YamlConfiguration();
    }

    public static void reloadConfig(String string) throws IOException {
        if(files.get(string) != null && configurations.get(string) != null){
            configurations.put(string, YamlConfiguration.loadConfiguration(files.get(string)));
        }else{
            loadConfig(string);
            if(files.get(string) != null && configurations.get(string) != null){
                configurations.put(string, YamlConfiguration.loadConfiguration(files.get(string)));
            }
        }
    }
}
