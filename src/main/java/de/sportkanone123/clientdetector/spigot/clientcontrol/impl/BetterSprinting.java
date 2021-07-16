package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BetterSprinting {

    public static void handle(Player player){

        /*1.13 - 1.16.5
         *** [byte 0] [bool <enableSurvivalFlyBoost>] [bool <enableAllDirs>]
         ***
         *** Notifies the player about which non-vanilla settings are enabled on the server (both are disabled by default).
         *** Sent to player when their [byte 0] message is processed, and either or both settings are enabled.
         *** Sent to all players with the mod after using the '/bettersprinting setting (...)' command.
         *
         *
         *** [byte 1]
         ***
         *** Disables basic functionality of the mod on client side.
         *** Sent to player when their [byte 0] message is processed, and the server wants to disable the mod.
         *** Sent to all players with the mod after using the '/bettersprinting disablemod true' command.
         *
         *
         *** [byte 2]
         ***
         *** Re-enables basic functionality of the mod on client side.
         *** Sent to all players with the mod after using the '/bettersprinting disablemod false' command.
         */

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        if(ConfigManager.getConfig("clientcontrol").getBoolean("bettersprinting.disableAll")){
            try {
                msgout.writeByte(1);
            } catch (IOException exception){

            }
        }else{
            try {
                msgout.writeByte(0);
                msgout.writeBoolean(!ConfigManager.getConfig("clientcontrol").getBoolean("bettersprinting.disableSurvivalFlyBoost"));
                msgout.writeBoolean(!ConfigManager.getConfig("clientcontrol").getBoolean("bettersprinting.disableAllDirs"));
            } catch (IOException exception){

            }
        }




        WrappedPacketOutCustomPayload costumPayload;
        if(PacketEvents.get().getPlayerUtils().getClientVersion(player).isNewerThanOrEquals(ClientVersion.v_1_13))
            costumPayload = new WrappedPacketOutCustomPayload("bsm:settings", msgbytes.toByteArray());
        else
            costumPayload = new WrappedPacketOutCustomPayload("BSM", msgbytes.toByteArray());

        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }

}
