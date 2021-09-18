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

/*
 * Credits:
 * AntiFastMath is based on AntiFastMath by MWHunter which is Licensed under MIT License.
 * Author: MWHunter, edited by Sportkanone123
 * GitHub: https://github.com/MWHunter/AntiFastMath
 */


package de.sportkanone123.clientdetector.spigot.hackdetector.impl;

import de.sportkanone123.clientdetector.spigot.ClientDetector;
import de.sportkanone123.clientdetector.spigot.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.function.Consumer;

public class AntiFastMath {
    static HashMap<Player, Double> vanillaPrecision = new HashMap<>();
    static HashMap<Player, Double> fastMathPrecision = new HashMap<>();
    static HashMap<Player, Integer> playerSamples = new HashMap<>();
    static HashMap<Player, Boolean> checkPlayer = new HashMap<>();

    static int multiplier = 1000; //10000

    public static void handleMovement(Player player, PlayerMoveEvent event){
        if(ConfigManager.getConfig("config").getBoolean("hackdetector.antifastmath.enableAntiFastMath")){
            if(checkPlayer.get(player) != null && checkPlayer.get(player)){
                Vector movement = event.getTo().toVector().subtract(event.getFrom().toVector());
                Vector vanillaMovement = getVanillaMathMovement(movement, (float) (0.1), event.getFrom().getYaw());
                Vector fastMathMovement = getFastMathMovement(movement, (float) (0.1), event.getFrom().getYaw());

                double lowVanilla = Math.min(Math.abs(vanillaMovement.getX()), Math.abs(vanillaMovement.getZ()));
                double lowOptifine = Math.min(Math.abs(fastMathMovement.getX()), Math.abs(fastMathMovement.getZ()));

                double vanillaRunning = vanillaPrecision.get(event.getPlayer());
                double optifineRunning = fastMathPrecision.get(event.getPlayer());

                double xDistance = event.getFrom().getX() - event.getTo().getX();
                double zDistance = event.getFrom().getZ() - event.getTo().getZ();

                if ((lowVanilla < 1e-5 || lowOptifine < 1e-5) && ((xDistance * xDistance) + (zDistance * zDistance) > 0.01)) {
                    vanillaRunning = vanillaRunning * 15 / 16 + lowVanilla;
                    optifineRunning = optifineRunning * 15 / 16 + lowOptifine;

                    vanillaPrecision.put(event.getPlayer(), vanillaRunning);
                    fastMathPrecision.put(event.getPlayer(), optifineRunning);

                    int count = playerSamples.get(event.getPlayer());
                    playerSamples.put(event.getPlayer(), count + 1);

                    if (count == ConfigManager.getConfig("config").getInt("hackdetector.antifastmath.checkAfterCounts") && optifineRunning * multiplier < vanillaRunning) {
                        checkPlayer.put(player, false);

                        for(Player player1 : Bukkit.getOnlinePlayers()){
                            if(player1.hasPermission(ConfigManager.getConfig("config").getString("alerts.notificationPermission"))){
                                player1.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("message").getString("hackdetector.antifastmath.usingfastmath").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", player.getName()).replace("%player_uuid%", player.getUniqueId().toString())));
                            }
                        }

                        if(ConfigManager.getConfig("config").getBoolean("hackdetector.antifastmath.enablePunishment")){
                            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                            scheduler.runTask(ClientDetector.plugin, () -> {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig("config").getString("hackdetector.antifastmath.punishCommand").replace("%prefix%", ConfigManager.getConfig("message").getString("prefix")).replace("%player_name%", player.getName()).replace("%player_uuid%", player.getUniqueId().toString())));
                            });
                        }
                    }
                }
            }
        }
    }

    public static void handleJoin(Player player, PlayerJoinEvent event){
        vanillaPrecision.put(player, 0D);
        fastMathPrecision.put(player, 0D);
        playerSamples.put(player, 0);
        checkPlayer.put(player, true);
    }

    public static void handleQuit(Player player, PlayerQuitEvent event){
        vanillaPrecision.remove(event.getPlayer());
        fastMathPrecision.remove(event.getPlayer());
        playerSamples.remove(event.getPlayer());
        checkPlayer.put(player, false);
    }

    public static void load(){
        OptifineMath.load();

        for(Player player : Bukkit.getOnlinePlayers()){
            vanillaPrecision.put(player, 0D);
            fastMathPrecision.put(player, 0D);
            playerSamples.put(player, 0);
            checkPlayer.put(player, true);
        }
    }

    public static Vector getVanillaMathMovement(Vector wantedMovement, float f, float f2) {
        float f3 = VanillaMath.sin(f2 * 0.017453292f);
        float f4 = VanillaMath.cos(f2 * 0.017453292f);

        float bestTheoreticalX = (float) (f3 * wantedMovement.getZ() + f4 * wantedMovement.getX()) / (f3 * f3 + f4 * f4) / f;
        float bestTheoreticalZ = (float) (-f3 * wantedMovement.getX() + f4 * wantedMovement.getZ()) / (f3 * f3 + f4 * f4) / f;

        return new Vector(bestTheoreticalX, 0, bestTheoreticalZ);
    }

    public static Vector getFastMathMovement(Vector wantedMovement, float f, float f2) {
        float f3 = OptifineMath.sin(f2 * 0.017453292f);
        float f4 = OptifineMath.cos(f2 * 0.017453292f);

        float bestTheoreticalX = (float) (f3 * wantedMovement.getZ() + f4 * wantedMovement.getX()) / (f3 * f3 + f4 * f4) / f;
        float bestTheoreticalZ = (float) (-f3 * wantedMovement.getX() + f4 * wantedMovement.getZ()) / (f3 * f3 + f4 * f4) / f;

        return new Vector(bestTheoreticalX, 0, bestTheoreticalZ);
    }

    public static class VanillaMath {
        private static final float[] SIN = make(new float[65536], arrf -> {
            for (int i = 0; i < arrf.length; ++i) {
                arrf[i] = (float) Math.sin((double) i * 3.141592653589793 * 2.0 / 65536.0);
            }
        });

        public static float sin(float f) {
            return SIN[(int) (f * 10430.378f) & 0xFFFF];
        }

        public static float cos(float f) {
            return SIN[(int) (f * 10430.378f + 16384.0f) & 0xFFFF];
        }

        public static float sqrt(float f) {
            return (float) Math.sqrt(f);
        }

        public static <T> T make(T t, Consumer<T> consumer) {
            consumer.accept(t);
            return t;
        }
    }

    public static class OptifineMath {
        private static final float[] SIN_TABLE_FAST = new float[4096];
        private static final float radToIndex = roundToFloat(651.8986469044033D);

        public static void load(){
            for (int j = 0; j < SIN_TABLE_FAST.length; ++j) {
                SIN_TABLE_FAST[j] = roundToFloat(Math.sin((double) j * Math.PI * 2.0D / 4096.0D));
            }
        }

        public static float sin(float value) {
            return SIN_TABLE_FAST[(int) (value * radToIndex) & 4095];
        }

        public static float cos(float value) {
            return SIN_TABLE_FAST[(int) (value * radToIndex + 1024.0F) & 4095];
        }

        public static float roundToFloat(double d) {
            return (float) ((double) Math.round(d * 1.0E8D) / 1.0E8D);
        }
    }
}