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







    public static class ModSettingsAdapter implements JsonDeserializer<ModSettings>, JsonSerializer<ModSettings>
    {
        @Override
        public ModSettings deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final ModSettings settings = new ModSettings();
            if (!jsonElement.isJsonObject()) {
                return settings;
            }
            final JsonObject object = jsonElement.getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (!entry.getValue().isJsonObject()) {
                    continue;
                }
                final JsonObject modSettingObject = entry.getValue().getAsJsonObject();
                settings.getModSettings().put(entry.getKey(), this.deserializeModSetting(modSettingObject));
            }
            return settings;
        }

        @Override
        public JsonElement serialize(final ModSettings modSettings, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject object = new JsonObject();
            for (final Map.Entry<String, ModSettings.ModSetting> entry : modSettings.getModSettings().entrySet()) {
                object.add(entry.getKey(), this.serializeModSetting(entry.getValue()));
            }
            return object;
        }

        private JsonObject serializeModSetting(final ModSettings.ModSetting setting) {
            final JsonObject object = new JsonObject();
            final JsonObject properties = new JsonObject();
            object.addProperty("enabled", setting.isEnabled());
            for (final Map.Entry<String, Object> entry : setting.getProperties().entrySet()) {
                JsonPrimitive primitive;
                if (entry.getValue() instanceof Boolean) {
                    primitive = new JsonPrimitive(String.valueOf(entry.getValue()));
                }
                else if (entry.getValue() instanceof String) {
                    primitive = new JsonPrimitive(String.valueOf(entry.getValue()));
                }
                else {
                    if (!(entry.getValue() instanceof Number)) {
                        continue;
                    }
                    primitive = new JsonPrimitive(String.valueOf(entry.getValue()));
                }
                properties.add(entry.getKey(), primitive);
            }
            object.add("properties", properties);
            return object;
        }

        private ModSettings.ModSetting deserializeModSetting(final JsonObject object) {
            final JsonObject propertiesObject = object.get("properties").getAsJsonObject();
            final Map<String, Object> properties = new HashMap<String, Object>();
            for (final Map.Entry<String, JsonElement> entry : propertiesObject.entrySet()) {
                if (!entry.getValue().isJsonPrimitive()) {
                    continue;
                }
                final JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                Object toSet;
                if (primitive.isString()) {
                    toSet = primitive.getAsString();
                }
                else if (primitive.isNumber()) {
                    toSet = primitive.getAsNumber();
                }
                else {
                    if (!primitive.isBoolean()) {
                        continue;
                    }
                    toSet = primitive.getAsBoolean();
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

    public static final class LCPacketModSettings extends LCPacket
    {
        private ModSettings settings;

        public LCPacketModSettings() {
        }

        public LCPacketModSettings(final ModSettings modSettings) {
            this.settings = modSettings;
        }

        @Override
        public void write(final ByteBufWrapper buf) throws IOException {
            buf.writeString(ModSettings.GSON.toJson(this.settings));
        }

        @Override
        public void read(final ByteBufWrapper buf) throws IOException {
            this.settings = ModSettings.GSON.fromJson(buf.readString(), ModSettings.class);
        }

        @Override
        public void process(final LCNetHandler handler) {
            ((LCNetHandlerClient)handler).handleModSettings(this);
        }

        public ModSettings getSettings() {
            return this.settings;
        }
    }

    public static final class ByteBufWrapper
    {
        private final ByteBuf buf;

        public ByteBufWrapper(final ByteBuf buf) {
            this.buf = buf;
        }

        public void writeVarInt(int b) {
            while ((b & 0xFFFFFF80) != 0x0) {
                this.buf.writeByte((b & 0x7F) | 0x80);
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
                i |= (b & 0x7F) << chunk++ * 7;
                if (chunk > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while ((b & 0x80) == 0x80);
            return i;
        }

        public void writeString(final String s) {
            final byte[] arr = s.getBytes(StandardCharsets.UTF_8);
            this.writeVarInt(arr.length);
            this.buf.writeBytes(arr);
        }

        public String readString() {
            final int len = this.readVarInt();
            final byte[] buffer = new byte[len];
            this.buf.readBytes(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }

        public void writeUUID(final UUID uuid) {
            this.buf.writeLong(uuid.getMostSignificantBits());
            this.buf.writeLong(uuid.getLeastSignificantBits());
        }

        public UUID readUUID() {
            final long mostSigBits = this.buf.readLong();
            final long leastSigBits = this.buf.readLong();
            return new UUID(mostSigBits, leastSigBits);
        }

        public ByteBuf buf() {
            return this.buf;
        }
    }

    public static abstract class LCPacket
    {
        private static final Map<Class, Integer> classToId;
        private static final Map<Integer, Class> idToClass;
        private Object attachment;

        public static LCPacket handle(final byte[] data) {
            return handle(data, null);
        }

        public static LCPacket handle(final byte[] data, final Object attachment) {
            final ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.wrappedBuffer(data));
            final int packetId = wrappedBuffer.readVarInt();
            final Class packetClass = LCPacket.idToClass.get(packetId);
            if (packetClass != null) {
                try {
                    final LCPacket packet = (LCPacket) packetClass.newInstance();
                    packet.attach(attachment);
                    packet.read(wrappedBuffer);
                    return packet;
                }
                catch (IOException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        public static byte[] getPacketData(final LCPacket packet) {
            return getPacketBuf(packet).array();
        }

        public static ByteBuf getPacketBuf(final LCPacket packet) {
            final ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.buffer());
            wrappedBuffer.writeVarInt(LCPacket.classToId.get(packet.getClass()));
            try {
                packet.write(wrappedBuffer);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            return wrappedBuffer.buf();
        }

        private static void addPacket(final int id, final Class clazz) {
            if (LCPacket.classToId.containsKey(clazz)) {
                throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + LCPacket.classToId.get(clazz));
            }
            if (LCPacket.idToClass.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + LCPacket.idToClass.get(id).getSimpleName());
            }
            LCPacket.classToId.put(clazz, id);
            LCPacket.idToClass.put(id, clazz);
        }

        public abstract void write(final ByteBufWrapper p0) throws IOException;

        public abstract void read(final ByteBufWrapper p0) throws IOException;

        public abstract void process(final LCNetHandler p0);

        public <T> void attach(final T obj) {
            this.attachment = obj;
        }

        public <T> T getAttachment() {
            return (T)this.attachment;
        }

        protected void writeBlob(final ByteBufWrapper b, final byte[] bytes) {
            b.buf().writeShort(bytes.length);
            b.buf().writeBytes(bytes);
        }

        protected byte[] readBlob(final ByteBufWrapper b) {
            final short key = b.buf().readShort();
            if (key < 0) {
                System.out.println("Key was smaller than nothing!  Weird key!");
                return null;
            }
            final byte[] blob = new byte[key];
            b.buf().readBytes(blob);
            return blob;
        }

        static {
            classToId = new HashMap<Class, Integer>();
            idToClass = new HashMap<Integer, Class>();
            addPacket(31, LCPacketModSettings.class);
        }
    }

    public static final class ModSettings
    {
        public static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(ModSettings.class, new ModSettingsAdapter()).create();
        private Map<String, ModSetting> modSettings;

        public ModSettings() {
            this.modSettings = new HashMap<String, ModSetting>();
        }

        public ModSettings addModSetting(final String modId, final ModSetting setting) {
            this.modSettings.put(modId, setting);
            return this;
        }

        public ModSetting getModSetting(final String modId) {
            return this.modSettings.get(modId);
        }

        public Map<String, ModSetting> getModSettings() {
            return this.modSettings;
        }

        public static class ModSetting
        {
            private boolean enabled;
            private Map<String, Object> properties;

            public ModSetting() {
            }

            public ModSetting(final boolean enabled, final Map<String, Object> properties) {
                this.enabled = enabled;
                this.properties = properties;
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public Map<String, Object> getProperties() {
                return this.properties;
            }

            @Override
            public String toString() {
                return "ModSetting{enabled=" + this.enabled + ", properties=" + this.properties + '}';
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final ModSetting that = (ModSetting)o;
                return this.enabled == that.enabled && Objects.equals(this.properties, that.properties);
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.enabled, this.properties);
            }
        }
    }

}
