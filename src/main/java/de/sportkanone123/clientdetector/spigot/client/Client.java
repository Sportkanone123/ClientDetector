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

package de.sportkanone123.clientdetector.spigot.client;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.listener.PluginMessageListener;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Client {
    private List<String> channel;
    private List<String> data;
    private String clientName;
    private Boolean onlyChannel;
    private Boolean hasVersion;
    private String splitCharacter;
    private Integer versionIndex;

    public Client(List<String> channel, String data, String clientName, Boolean onlyChannel, Boolean hasVersion, String splitCharacter, Integer versionIndex){
        this.channel = channel;
        this.data = Arrays.asList(data);
        this.clientName = clientName;
        this.onlyChannel = onlyChannel;
        this.hasVersion = hasVersion;
        this.splitCharacter = splitCharacter;
        this.versionIndex = versionIndex;
    }

    public Client(String channel, String data, String clientName, Boolean onlyChannel, Boolean hasVersion, String splitCharacter, Integer versionIndex){
        this.channel = Arrays.asList(channel);
        this.data = Arrays.asList(data);
        this.clientName = clientName;
        this.onlyChannel = onlyChannel;
        this.hasVersion = hasVersion;
        this.splitCharacter = splitCharacter;
        this.versionIndex = versionIndex;
    }

    public Client(List<String> channel, List<String> data, String clientName, Boolean onlyChannel, Boolean hasVersion, String splitCharacter, Integer versionIndex){
        this.channel = channel;
        this.data = data;
        this.clientName = clientName;
        this.onlyChannel = onlyChannel;
        this.hasVersion = hasVersion;
        this.splitCharacter = splitCharacter;
        this.versionIndex = versionIndex;
    }

    public Client(String channel, List<String> data, String clientName, Boolean onlyChannel, Boolean hasVersion, String splitCharacter, Integer versionIndex){
        this.channel = Arrays.asList(channel);
        this.data = data;
        this.clientName = clientName;
        this.onlyChannel = onlyChannel;
        this.hasVersion = hasVersion;
        this.splitCharacter = splitCharacter;
        this.versionIndex = versionIndex;
    }

    public List<String> getChannel() { return channel; }

    public void setChannel(List<String> channel) { this.channel = channel; }

    public List<String> getData() { return data; }

    public void setData(List<String> data) { this.data = data; }

    public String getClientName() { return clientName; }

    public void setClientName(String clientName) { this.clientName = clientName; }

    public Boolean getOnlyChannel() { return onlyChannel; }

    public void setOnlyChannel(Boolean onlyChannel) { this.onlyChannel = onlyChannel; }

    public Boolean getHasVersion() { return hasVersion; }

    public void setHasVersion(Boolean hasVersion) { this.hasVersion = hasVersion; }

    public String getSplitCharacter() { return splitCharacter; }

    public void setSplitCharacter(String splitCharacter) { this.splitCharacter = splitCharacter; }

    public Integer getVersionIndex() { return versionIndex; }

    public void setVersionIndex(Integer versionIndex) { this.versionIndex = versionIndex; }

    public Boolean isClient(String channel, byte[] data){
        if(this.onlyChannel) {
            for (String ch : this.channel) {
                if (channel.equalsIgnoreCase(ch)) return true;
            }
            return false;
        }else {
            for (String ch : this.channel) {
                for(String dat : this.data){
                    if (channel.equalsIgnoreCase(ch) && new String(data, StandardCharsets.UTF_8).contains(dat)) return true;
                }
            }
            return false;
        }
    }

    public String getClientName(String channel, byte[] data){
        if(isClient(channel, data))
            return this.clientName;
        else
            return null;
    }

    public String getVersion(String channel, byte[] data){
        if(isClient(channel, data)) {
            String dataString = new String(data, StandardCharsets.UTF_8);
            return dataString.split(this.splitCharacter)[this.versionIndex];
        }else {
            return null;
        }
    }

    public void load(){
        for(String str : this.channel){
            if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)){
                if(str.contains(":")){
                    Bukkit.getMessenger().registerIncomingPluginChannel(ClientDetector.plugin, str, new PluginMessageListener());
                }
            }else{
                Bukkit.getMessenger().registerIncomingPluginChannel(ClientDetector.plugin, str, new PluginMessageListener());
            }
        }
    }
}
