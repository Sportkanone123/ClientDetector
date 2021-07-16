package de.sportkanone123.clientdetector.spigot.mod;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.listener.PluginMessageListener;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mod {
    private List<String> channel;
    private List<String> data;
    private String modName;
    private Boolean onlyChannel;

    public Mod(List<String> channel, String data, String modName, Boolean onlyChannel){
        this.channel = channel;
        this.data = Arrays.asList(data);
        this.modName = modName;
        this.onlyChannel = onlyChannel;
    }

    public Mod(String channel, String data, String modName, Boolean onlyChannel){
        this.channel = Arrays.asList(channel);
        this.data = Arrays.asList(data);
        this.modName = modName;
        this.onlyChannel = onlyChannel;
    }

    public Mod(List<String> channel, List<String> data, String modName, Boolean onlyChannel){
        this.channel = channel;
        this.data = data;
        this.modName = modName;
        this.onlyChannel = onlyChannel;
    }

    public Mod(String channel, List<String> data, String modName, Boolean onlyChannel){
        this.channel = Arrays.asList(channel);
        this.data = data;
        this.modName = modName;
        this.onlyChannel = onlyChannel;
    }

    public List<String> getChannel() { return channel; }

    public void setChannel(List<String> channel) { this.channel = channel; }

    public List<String> getData() { return data; }

    public void setData(List<String> data) { this.data = data; }

    public String getModName() { return modName; }

    public void setModName(String modName) { this.modName = modName; }

    public Boolean getOnlyChannel() { return onlyChannel; }

    public void setOnlyChannel(Boolean onlyChannel) { this.onlyChannel = onlyChannel; }

    public Boolean isMod(String channel, byte[] data){
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

    public String getModName(String channel, byte[] data){
        if(isMod(channel, data))
            return this.modName;
        else
            return null;
    }

    public void load(){
        for(String str : this.channel) {
            if(PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)){
                if(str.contains(":")){
                    Bukkit.getMessenger().registerIncomingPluginChannel(ClientDetector.plugin, str, new PluginMessageListener());
                }
            }else{
                Bukkit.getMessenger().registerIncomingPluginChannel(ClientDetector.plugin, str, new PluginMessageListener());
            }
        }
    }
}
