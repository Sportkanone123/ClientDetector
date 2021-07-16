package de.sportkanone123.clientdetector.spigot.client;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.listener.PluginMessageListener;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
