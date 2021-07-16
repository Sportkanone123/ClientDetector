package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WorldDownloader {
    public static void handle(Player player){
        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeInt(0);
            msgout.writeBoolean(!ConfigManager.getConfig("clientcontrol").getBoolean("worlddownloader.disableAll"));
        } catch (IOException exception){

        }

        WrappedPacketOutCustomPayload costumPayload;

        if(PacketEvents.get().getPlayerUtils().getClientVersion(player).isNewerThanOrEquals(ClientVersion.v_1_13)){
            costumPayload = new WrappedPacketOutCustomPayload("wdl:control", msgbytes.toByteArray());
            PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
        }else{
            costumPayload = new WrappedPacketOutCustomPayload("WDL|CONTROL", msgbytes.toByteArray());
            PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
        }
    }
}
