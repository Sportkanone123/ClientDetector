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

import com.google.gson.JsonObject;
import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
More information can be found here: https://docs.labymod.net/pages/server/introduction/
*/
public class LabyMod {
    public static void handlePacket(Player player, byte[] data){
        String strData = translateString(new String(data, StandardCharsets.UTF_8));
        if(strData.contains("voicechat")){

        }else if(strData.contains("INFO")){
            strData = strData.substring(strData.indexOf("INFO") + 6);

            try {
                JSONParser jsonParser = new JSONParser();
                Object parsed = jsonParser.parse(strData);
                JSONObject jsonObject = (JSONObject) parsed;

                String version = jsonObject.containsKey( "version" ) ? jsonObject.get( "version" ).toString() : "Unknown";
                if(!version.equalsIgnoreCase("Unknown")) ClientDetector.clientVersion.put(player, version);

                ArrayList<String> addons = new ArrayList<>();

                JSONArray jsonAddons = (JSONArray) jsonObject.get( "addons");

                for(int i = 0; i < jsonAddons.size(); i++){
                    JSONObject tempJsonObject = (JSONObject) jsonAddons.get(i);
                    if(ClientDetector.playerLabymodMods.get(player) == null){
                        ClientDetector.playerLabymodMods.put(player, new ArrayList<>());
                    }
                    ClientDetector.playerLabymodMods.get(player).add(tempJsonObject.get( "name" ).toString());
                }
            } catch (ParseException e) {

            }
        }
    }

    public static void handlePluginMessage(Player player, byte[] data){
        String strData = translateString(new String(data, StandardCharsets.UTF_8));
        if(strData.contains("voicechat")){

        }else if(strData.contains("INFO")){
            strData = strData.substring(strData.indexOf("INFO") + 6);

            try {
                JSONParser jsonParser = new JSONParser();
                Object parsed = jsonParser.parse(strData);
                JSONObject jsonObject = (JSONObject) parsed;

                String version = jsonObject.containsKey( "version" ) ? jsonObject.get( "version" ).toString() : "Unknown";
                if(!version.equalsIgnoreCase("Unknown")) ClientDetector.clientVersion.put(player, version);

                ArrayList<String> addons = new ArrayList<>();

                JSONArray jsonAddons = (JSONArray) jsonObject.get( "addons");

                for(int i = 0; i < jsonAddons.size(); i++){
                    JSONObject tempJsonObject = (JSONObject) jsonAddons.get(i);
                    if(ClientDetector.playerLabymodMods.get(player) == null){
                        ClientDetector.playerLabymodMods.put(player, new ArrayList<>());
                    }
                    ClientDetector.playerLabymodMods.get(player).add(tempJsonObject.get( "name" ).toString());
                }
            } catch (ParseException e) {

            }
        }
    }

    public static void hande(Player player){
        Map<Permission, Boolean> modifiedPermissions = new HashMap<>();
        for(Permission perm:Permission.values()){
            if(perm.equals(Permission.ANIMATIONS)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableAnimations"));
            }else if(perm.equals(Permission.BLOCKBUILD)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.diableBlockBuild"));
            }else if(perm.equals(Permission.CHAT)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableChat"));
            }else if(perm.equals(Permission.CROSSHAIR_SYNC)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableCrosshairSync"));
            }else if(perm.equals(Permission.GUI_ARMOR_HUD)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.gui.disableArmorHud"));
            }else if(perm.equals(Permission.GUI_ITEM_HUD)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.gui.disableItemHud"));
            }else if(perm.equals(Permission.GUI_POTION_EFFECTS)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.gui.disablePotionEffects"));
            }else if(perm.equals(Permission.IMPROVED_LAVA)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableImprovedLava"));
            }else if(perm.equals(Permission.REFILL_FIX)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableRefillFix"));
            }else if(perm.equals(Permission.SATURATION_BAR)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableSaturationBar"));
            }else if(perm.equals(Permission.TAGS)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableTags"));
            }else if(perm.equals(Permission.GUI_ALL)){
                modifiedPermissions.put(perm, !ConfigManager.getConfig("clientcontrol").getBoolean("labymod.disableGuiAll"));
            }

            WrappedPacketOutCustomPayload costumPayload;

            if(PacketEvents.get().getPlayerUtils().getClientVersion(player).isNewerThanOrEquals(ClientVersion.v_1_13)){
                costumPayload = new WrappedPacketOutCustomPayload("labymod3:main",  LabyModProtocol.getBytesToSend(modifiedPermissions));
                PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
            }else{
                costumPayload = new WrappedPacketOutCustomPayload("LMC",  LabyModProtocol.getBytesToSend(modifiedPermissions));
                PacketEvents.get().getPlayerUtils().sendPacket(player, costumPayload);
            }
        }
    }



    public enum Permission {
        // Permissions that are disabled by default
        IMPROVED_LAVA( "Improved Lava", false ),
        CROSSHAIR_SYNC( "Crosshair sync", false ),
        REFILL_FIX( "Refill fix", false ),
        RANGE( "Range", false ), // CLASSIC PVP - 1.16 only
        SLOWDOWN( "Slowdown", false ), // CLASSIC PVP - 1.16 only

        // GUI permissions
        GUI_ALL( "LabyMod GUI", true ),
        GUI_POTION_EFFECTS( "Potion Effects", true ),
        GUI_ARMOR_HUD( "Armor HUD", true ),
        GUI_ITEM_HUD( "Item HUD", true ),

        // Permissions that are enabled by default
        BLOCKBUILD( "Blockbuild", true ),
        TAGS( "Tags", true ),
        CHAT( "Chat features", true ),
        ANIMATIONS( "Animations", true ),
        SATURATION_BAR( "Saturation bar", true );

        private String displayName;
        private boolean defaultEnabled;

        /**
         * @param displayName    the permission's display-name
         * @param defaultEnabled whether or not this permission is enabled/activated by default
         */
        Permission( String displayName, boolean defaultEnabled ) {
            this.displayName = displayName;
            this.defaultEnabled = defaultEnabled;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isDefaultEnabled() {
            return defaultEnabled;
        }
    }

    public static class LabyModProtocol {
        /**
         * Gets the bytes that are required to send the given message
         *
         * @param messageKey      the message's key
         * @param messageContents the message's contents
         * @return the byte array that should be the payload
         */
        public static byte[] getBytesToSend( String messageKey, String messageContents ) {
            // Getting an empty buffer
            ByteBuf byteBuf = Unpooled.buffer();

            // Writing the message-key to the buffer
            writeString( byteBuf, messageKey );

            // Writing the contents to the buffer
            writeString( byteBuf, messageContents );

            // Copying the buffer's bytes to the byte array
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes( bytes );

            // Release the buffer
            byteBuf.release();

            // Returning the byte array
            return bytes;
        }

        public static byte[] getBytesToSend( Map<Permission, Boolean> permissions ) {
            // Creating a json object we will put the permissions in
            JsonObject object = new JsonObject();

            // Adding the permissions to the json object
            for ( Map.Entry<Permission, Boolean> permissionEntry : permissions.entrySet() ) {
                object.addProperty( permissionEntry.getKey().name(), permissionEntry.getValue() );
            }

            // Returning the byte array
            return getBytesToSend( "PERMISSIONS", object.toString() );
        }

        /**
         * Writes a varint to the given byte buffer
         *
         * @param buf   the byte buffer the int should be written to
         * @param input the int that should be written to the buffer
         */
        private static void writeVarIntToBuffer( ByteBuf buf, int input ) {
            while ( (input & -128) != 0 ) {
                buf.writeByte( input & 127 | 128 );
                input >>>= 7;
            }

            buf.writeByte( input );
        }

        /**
         * Writes a string to the given byte buffer
         *
         * @param buf    the byte buffer the string should be written to
         * @param string the string that should be written to the buffer
         */
        private static void writeString( ByteBuf buf, String string ) {
            byte[] abyte = string.getBytes( Charset.forName( "UTF-8" ) );

            if ( abyte.length > Short.MAX_VALUE ) {
                throw new EncoderException( "String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")" );
            } else {
                writeVarIntToBuffer( buf, abyte.length );
                buf.writeBytes( abyte );
            }
        }
    }

    private static String translateString(String string) {
        String[] CharsToCorrect = new String[] { "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\t", "\u0010", "\u0011", "\u0012", "\u0014", "\u0015", "\u0016", "\u0017", "\u0019", "\u000f", "\u001a", "\u000e", "\u000b", "\u001b", "\f", "\r", "\b", "\u263a" + "\uff1f" + "\ufe56" + "\u2639" + "\u263b"};
        for (String str:CharsToCorrect) {
            string = string.replace(str, " ");
        }
        return string.replace("\t", " ").replace("  ", " ").replace("\n", " ").trim();
    }
}
