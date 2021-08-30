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

package de.sportkanone123.clientdetector.spigot.clientcontrol.impl;

import com.google.gson.*;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/*
More information can be found here: https://github.com/LunarClient/BukkitAPI
 */
public class LunarClient {
    public static void handle(Player player){
        ModSettings modSettings = new ModSettings();

        for(String string : ((MemorySection) ConfigManager.getConfig("clientcontrol").get("lunar_disable")).getKeys(false)) {
            if (ConfigManager.getConfig("clientcontrol").getBoolean("lunar_disable." + string))
                modSettings.addModSetting(string, new ModSettings.ModSetting(false, new HashMap<>()));
        }

        LCPacketModSettings packetModSettings = new LCPacketModSettings(modSettings);

        sendPacket(player, packetModSettings);
    }

    public static void sendPacket(final Player player, final LCPacket packet) {
        final UUID playerId = player.getUniqueId();
        final String channel = "lunarclient:pm";

        WrappedPacketOutCustomPayload costumPayload = new WrappedPacketOutCustomPayload(channel,  LCPacket.getPacketData(packet));
        PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
    }


    public static class ModSettingsAdapter implements JsonDeserializer<ModSettings>, JsonSerializer<ModSettings> {
        @Override
        public ModSettings deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            ModSettings settings = new ModSettings();
            if (!jsonElement.isJsonObject()) {
                return settings;
            }

            JsonObject object = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (!entry.getValue().isJsonObject()) {
                    continue;
                }
                JsonObject modSettingObject = entry.getValue().getAsJsonObject();

                settings.getModSettings().put(entry.getKey(), deserializeModSetting(modSettingObject));
            }
            return settings;
        }

        @Override
        public JsonElement serialize(ModSettings modSettings, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            for (Map.Entry<String, ModSettings.ModSetting> entry : modSettings.getModSettings().entrySet()) {
                object.add(entry.getKey(), serializeModSetting(entry.getValue()));
            }
            return object;
        }

        private JsonObject serializeModSetting(ModSettings.ModSetting setting) {
            JsonObject object = new JsonObject();
            JsonObject properties = new JsonObject();
            object.addProperty("enabled", setting.isEnabled());
            for (Map.Entry<String, Object> entry : setting.getProperties().entrySet()) {

                JsonPrimitive primitive;
                if (entry.getValue() instanceof Boolean) {
                    primitive = new JsonPrimitive((Boolean) entry.getValue());
                } else if (entry.getValue() instanceof String) {
                    primitive = new JsonPrimitive((String) entry.getValue());
                } else if (entry.getValue() instanceof Number) {
                    primitive = new JsonPrimitive((Number) entry.getValue());
                } else {
                    continue;
                }
                properties.add(entry.getKey(), primitive);
            }
            object.add("properties", properties);
            return object;
        }

        private ModSettings.ModSetting deserializeModSetting(JsonObject object) {
            JsonObject propertiesObject = object.get("properties").getAsJsonObject();
            Map<String, Object> properties = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : propertiesObject.entrySet()) {
                if (!entry.getValue().isJsonPrimitive()) {
                    continue;
                }
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                Object toSet;
                if (primitive.isString()) {
                    toSet = primitive.getAsString();
                } else if (primitive.isNumber()) {
                    toSet = primitive.getAsNumber();
                } else if (primitive.isBoolean()) {
                    toSet = primitive.getAsBoolean();
                } else {
                    continue;
                }
                properties.put(entry.getKey(), toSet);
            }
            return new ModSettings.ModSetting(object.get("enabled").getAsBoolean(), properties);
        }

    }

    public interface LCNetHandler
    {
        /*void handleAddWaypoint(final LCPacketWaypointAdd p0);

        void handleRemoveWaypoint(final LCPacketWaypointRemove p0);

        void handleEmote(final LCPacketEmoteBroadcast p0);*/
    }

    public interface LCNetHandlerClient extends LCNetHandler
    {
        void handleModSettings(final LCPacketModSettings p0);
    }

    public static final class LCPacketModSettings extends LCPacket {

        private ModSettings settings;

        public LCPacketModSettings() {}

        public LCPacketModSettings(ModSettings modSettings) {
            this.settings = modSettings;
        }


        @Override
        public void write(ByteBufWrapper buf) throws IOException {
            buf.writeString(ModSettings.GSON.toJson(this.settings));
        }

        @Override
        public void read(ByteBufWrapper buf) throws IOException {
            this.settings = ModSettings.GSON.fromJson(buf.readString(), ModSettings.class);
        }

        @Override
        public void process(LCNetHandler handler) {
            ((LCNetHandlerClient) handler).handleModSettings(this);
        }

        public ModSettings getSettings() {
            return settings;
        }
    }

    public static final class ByteBufWrapper {

        private final ByteBuf buf;

        public ByteBufWrapper(ByteBuf buf) {
            this.buf = buf;
        }

        public void writeVarInt(int b) {
            while ((b & -128) != 0) {
                this.buf.writeByte(b & 127 | 128);
                b >>>= 7;
            }

            this.buf.writeByte(b);
        }

        public int readVarInt() {
            int i = 0;
            int chunk = 0;
            byte b;

            do {
                b = this.buf.readByte();
                i |= (b & 127) << chunk++ * 7;

                if (chunk > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while ((b & 128) == 128);

            return i;
        }

        public void writeString(String s) {
            byte[] arr = s.getBytes(StandardCharsets.UTF_8);

            this.writeVarInt(arr.length);
            this.buf.writeBytes(arr);
        }

        public String readString() {
            int len = readVarInt();

            byte[] buffer = new byte[len];
            buf.readBytes(buffer);

            return new String(buffer, StandardCharsets.UTF_8);
        }

        public void writeUUID(UUID uuid) {
            this.buf.writeLong(uuid.getMostSignificantBits());
            this.buf.writeLong(uuid.getLeastSignificantBits());
        }

        public UUID readUUID() {
            long mostSigBits = this.buf.readLong();
            long leastSigBits = this.buf.readLong();

            return new UUID(mostSigBits, leastSigBits);
        }

        public ByteBuf buf() {
            return buf;
        }

    }

    public static abstract class LCPacket {

        private static final Map<Class, Integer> classToId = new HashMap<>();
        private static final Map<Integer, Class> idToClass = new HashMap<>();

        static {
            /*// server
            addPacket(0, LCPacketClientVoice.class);
            addPacket(16, LCPacketVoice.class);
            addPacket(1, LCPacketVoiceChannelSwitch.class);
            addPacket(2, LCPacketVoiceMute.class);

            addPacket(17, LCPacketVoiceChannel.class);
            addPacket(18, LCPacketVoiceChannelRemove.class);
            addPacket(19, LCPacketVoiceChannelUpdate.class);

            // client
            addPacket(3, LCPacketCooldown.class);
            addPacket(4, LCPacketHologram.class);
            addPacket(6, LCPacketHologramRemove.class);
            addPacket(5, LCPacketHologramUpdate.class);
            addPacket(7, LCPacketNametagsOverride.class);
            addPacket(8, LCPacketNametagsUpdate.class);
            addPacket(9, LCPacketNotification.class);
            addPacket(10, LCPacketServerRule.class);
            addPacket(11, LCPacketServerUpdate.class);
            addPacket(12, LCPacketStaffModState.class);
            addPacket(13, LCPacketTeammates.class);
            addPacket(14, LCPacketTitle.class);
            addPacket(15, LCPacketUpdateWorld.class);
            addPacket(20, LCPacketWorldBorder.class);
            addPacket(21, LCPacketWorldBorderRemove.class);
            addPacket(22, LCPacketWorldBorderUpdate.class);
            addPacket(25, LCPacketGhost.class);
            addPacket(28, LCPacketBossBar.class);
            addPacket(29, LCPacketWorldBorderCreateNew.class);
            addPacket(30, LCPacketWorldBorderUpdateNew.class);*/
            addPacket(31, LCPacketModSettings.class);

            /*// shared
            addPacket(26, LCPacketEmoteBroadcast.class);
            addPacket(23, LCPacketWaypointAdd.class);
            addPacket(24, LCPacketWaypointRemove.class);*/
        }

        private Object attachment;

        public static LCPacket handle(byte[] data) {
            return handle(data, null);
        }

        public static LCPacket handle(byte[] data, Object attachment) {
            ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.wrappedBuffer(data));

            int packetId = wrappedBuffer.readVarInt();
            Class packetClass = idToClass.get(packetId);

            if (packetClass != null) {
                try {
                    LCPacket packet = (LCPacket) packetClass.newInstance();

                    packet.attach(attachment);
                    packet.read(wrappedBuffer);

                    return packet;
                } catch (IOException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }

            return null;
        }

        public static byte[] getPacketData(LCPacket packet) {
            return getPacketBuf(packet).array();
        }

        public static ByteBuf getPacketBuf(LCPacket packet) {
            ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.buffer());
            wrappedBuffer.writeVarInt(classToId.get(packet.getClass()));

            try {
                packet.write(wrappedBuffer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return wrappedBuffer.buf();
        }

        private static void addPacket(int id, Class clazz) {
            if (classToId.containsKey(clazz)) {
                throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + classToId.get(clazz));
            } else if (idToClass.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + idToClass.get(id).getSimpleName());
            }

            classToId.put(clazz, id);
            idToClass.put(id, clazz);
        }

        public abstract void write(ByteBufWrapper buf) throws IOException;

        public abstract void read(ByteBufWrapper buf) throws IOException;

        public abstract void process(LCNetHandler handler);

        public <T> void attach(T obj) {
            this.attachment = obj;
        }

        @SuppressWarnings("unchecked")
        public <T> T getAttachment() {
            return (T) attachment;
        }

        protected void writeBlob(ByteBufWrapper b, byte[] bytes) {
            b.buf().writeShort(bytes.length);
            b.buf().writeBytes(bytes);
        }

        protected byte[] readBlob(ByteBufWrapper b) {
            short key = b.buf().readShort();

            if (key < 0) {
                System.out.println("Key was smaller than nothing!  Weird key!");
            } else {
                byte[] blob = new byte[key];
                b.buf().readBytes(blob);
                return blob;
            }

            return null;
        }

    }

    public static final class ModSettings {

        public static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(ModSettings.class, new ModSettingsAdapter()).create();

        private final Map<String, ModSetting> modSettings = new HashMap<>();

        public ModSettings addModSetting(String modId, ModSetting setting) {
            modSettings.put(modId, setting);
            return this;
        }

        public ModSetting getModSetting(String modId) {
            return this.modSettings.get(modId);
        }

        public Map<String, ModSetting> getModSettings() {
            return modSettings;
        }

        public static class ModSetting {
            private boolean enabled;
            private Map<String, Object> properties;

            public ModSetting() { } // for serialization

            public ModSetting(boolean enabled, Map<String, Object> properties) {
                this.enabled = enabled;
                this.properties = properties;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public Map<String, Object> getProperties() {
                return properties;
            }

            @Override
            public String toString() {
                return "ModSetting{" +
                        "enabled=" + enabled +
                        ", properties=" + properties +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ModSetting that = (ModSetting) o;
                return enabled == that.enabled &&
                        Objects.equals(properties, that.properties);
            }

            @Override
            public int hashCode() {
                return Objects.hash(enabled, properties);
            }
        }

    }
}
