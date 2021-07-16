package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;
import org.geysermc.connector.utils.FileUtils;

import java.util.BitSet;

public class FiveZig {
    public static void handle(Player player){
        boolean[] features = new boolean[6];

        features[0] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePotionEffectHud");
        features[1] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePotionIndicatorVignette");
        features[2] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableArmourHud");
        features[3] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disablePlayerSaturation");
        features[4] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableEntityHealthIndicator");
        features[5] = ConfigManager.getConfig("clientcontrol").getBoolean("fivezig.disableAutoReconnect");

        final BitSet disableBitSet = new BitSet();

        // Set the according bits
        for (int i = 0; i < features.length; ++i) {
            disableBitSet.set(i, features[i]);
        }

        WrappedPacketOutCustomPayload costumPayload;

        if(PacketEvents.get().getPlayerUtils().getClientVersion(player).isNewerThanOrEquals(ClientVersion.v_1_13)){
            costumPayload = new WrappedPacketOutCustomPayload("the5zigmod:5zig_set",  disableBitSet.toByteArray());
            PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);

            costumPayload = new WrappedPacketOutCustomPayload("l:5zig_set",  disableBitSet.toByteArray());
            PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
        }else{
            costumPayload = new WrappedPacketOutCustomPayload("5zig_Set",  disableBitSet.toByteArray());
            PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
        }

    }
}
