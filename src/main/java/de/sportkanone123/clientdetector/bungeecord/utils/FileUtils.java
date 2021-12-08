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

package de.sportkanone123.clientdetector.bungeecord.utils;

import de.sportkanone123.clientdetector.bungeecord.ClientDetectorBungee;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

public class FileUtils {
    private static final Plugin plugin = ClientDetectorBungee.plugin;
    static HashMap<String , File> files = new HashMap<String , File>();
    static HashMap<String , Configuration> configurations = new HashMap<String , Configuration>();

    public static void loadConfig(String string){
        File file = new File(plugin.getDataFolder(), string + ".yml");

        try{
            if(!file.exists()){
                InputStream in = plugin.getResourceAsStream(string + ".yml");
                Files.copy(in, file.toPath());
            }

            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            files.put(string, file);
            configurations.put(string, configuration);
        } catch(IOException e){
            System.out.println("[ClientDetector] Error loading files, please contact the developer and send him this stacktrace:");
            e.printStackTrace();
        }
    }

    public static void saveConfig(String string){
        try {
            if(files.get(string) != null && configurations.get(string) != null){
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configurations.get(string), files.get(string));
                configurations.put(string, ConfigurationProvider.getProvider(YamlConfiguration.class).load(files.get(string)));
            }else{
                loadConfig(string);
                if(files.get(string) != null && configurations.get(string) != null){
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(configurations.get(string), files.get(string));
                    configurations.put(string, ConfigurationProvider.getProvider(YamlConfiguration.class).load(files.get(string)));
                }
            }
        } catch (IOException e) {
            System.out.println("[ClientDetector] Error saving files, please contact the developer and send him this stacktrace:");
            e.printStackTrace();
        }
    }

    public static Configuration getConfig(String string){
        if(configurations.get(string) != null){
            return configurations.get(string);
        }else{
            loadConfig(string);
            if(configurations.get(string) != null){
                return configurations.get(string);
            }
        }
        return new Configuration();
    }
}
