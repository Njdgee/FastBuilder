package com.njdge.fastbuilder.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerUtils {

    public static String getRealNameFromUuid(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                if (jsonResponse.has("name")) {
                    String realName = jsonResponse.get("name").getAsString();
                    return realName;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealName(String name) {
        try {
            String mojangAPIUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;
            URL url = new URL(mojangAPIUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                if (jsonResponse.has("name")) {
                    String realName = jsonResponse.get("name").getAsString();
                    return realName;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isExisting(String name) {
        try {
            String mojangAPIUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;
            URL url = new URL(mojangAPIUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                if (jsonResponse.has("name")) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UUID getUUID(String name) {
        try {
            String mojangAPIUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;
            URL url = new URL(mojangAPIUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                if (jsonResponse.has("id")) {
                    UUID uuid = getProperUUID(jsonResponse.get("id").getAsString());
                    return uuid;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpURLConnection getGetConnection(String url) throws IOException {
        URL statusURL = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) statusURL.openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

    private static UUID getProperUUID(String uuidString){
        if(uuidString.length() != 32){
            return null;
        }

        String first = uuidString.substring(0,8);
        String second = uuidString.substring(8,12);
        String third = uuidString.substring(12,16);
        String fourth = uuidString.substring(16,20);
        String fifth = uuidString.substring(20);

        return UUID.fromString(first+"-"+second+"-"+third+"-"+fourth+"-"+fifth);
    }

    public static void reset(Player player, boolean resetHeldSlot) {
        if (player == null) {
            return;
        }
        player.setHealth(20.0D);
        player.setSaturation(0.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setItemOnCursor(null);
        player.getOpenInventory().getTopInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.spigot().setCollidesWithEntities(true);


        //Since Player#sendTitle only reset the title, not subtitle, so we do some tricky stuff here
        TitleSender.sendTitle(player, "&r", PacketPlayOutTitle.EnumTitleAction.TITLE, 1, 10, 1);
        TitleSender.sendTitle(player, "&r", PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 1, 10, 1);

        if (resetHeldSlot) {
            player.getInventory().setHeldItemSlot(0);
        }

        player.updateInventory();
    }

}
