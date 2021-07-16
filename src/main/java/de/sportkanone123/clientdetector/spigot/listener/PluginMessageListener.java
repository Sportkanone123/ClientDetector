package de.sportkanone123.clientdetector.spigot.listener;

import org.bukkit.entity.Player;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        de.sportkanone123.clientdetector.spigot.mod.processor.PluginMessageProcessor.handlePluginMessage(player, channel, data);
        de.sportkanone123.clientdetector.spigot.client.processor.PluginMessageProcessor.handlePluginMessage(player, channel, data);

        de.sportkanone123.clientdetector.spigot.manager.AlertsManager.handle(player, channel, data);

        de.sportkanone123.clientdetector.spigot.manager.BungeeManager.handle(player, channel, data);

        de.sportkanone123.clientdetector.spigot.clientcontrol.ClientControl.handlePluginMessage(player, channel, data);

        de.sportkanone123.clientdetector.spigot.forgemod.legacy.ForgeHandler.handle(player, channel, data);

    }
}
