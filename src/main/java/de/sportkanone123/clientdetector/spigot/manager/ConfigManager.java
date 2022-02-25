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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.HashMap;

public class ConfigManager {
    private static final Plugin plugin = ClientDetector.plugin ;
    static HashMap<String , File> files = new HashMap<String , File>();
    static HashMap<String , FileConfiguration> configurations = new HashMap<String , FileConfiguration>();

    public static void loadConfig(String string) throws IOException {
        File file = new File(plugin.getDataFolder(), string + ".yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                plugin.saveResource(string + ".yml", false);
            }catch (IllegalArgumentException e){

            }
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

    public static void optimizeConfig(String config, String path, Object value){
        File file = new File(plugin.getDataFolder(), "data" + File.separator + "log" + ".yml");

        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                if(!getConfig(config).get(path).equals(value) && !getConfig("data" + File.separator + "log").contains("automation.config_optimization." + path.replace(".", "_"))){
                    getConfig(config).set(path, value);
                    getConfig("data" + File.separator + "log").set("automation.config_optimization." + path.replace(".", "_"), true);

                    saveConfig(config);
                    saveConfig("data" + File.separator + "log");

                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3ClientDetector&7] (&aConfigOptimizer&7) &c We detected a problem with your configuration, it will be updated automatically: Config file: '" + config + ".yml', path: '" + path + "'"));
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
}
