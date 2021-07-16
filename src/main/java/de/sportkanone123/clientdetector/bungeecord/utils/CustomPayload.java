package de.sportkanone123.clientdetector.bungeecord.utils;

import java.util.UUID;

public class CustomPayload {
    private UUID uuid;
    private String channel;
    private byte[] data;

    public CustomPayload(final UUID uuid, final String channel, final byte[] data) {
        this.uuid = uuid;
        this.channel = channel;
        this.data = data;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    public String getChannel() {
        return this.channel;
    }

    public byte[] getData() {
        return this.data;
    }
}
