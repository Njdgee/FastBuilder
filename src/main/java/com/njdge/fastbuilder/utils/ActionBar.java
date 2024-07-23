package com.njdge.fastbuilder.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar {

    private static String nmsVersion;

    public static void sendActionBar(Player player, String message) {
        if(nmsVersion == null) {
            nmsVersion = (nmsVersion = Bukkit.getServer().getClass().getPackage().getName()).substring(nmsVersion.lastIndexOf(".") + 1);
        }
        try {
            Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            Object p = c1.cast(player);
            Object ppoc;
            Class<?> c2, c3,
                    c4 = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat"),
                    c5 = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            Object o;
            if ((nmsVersion.equalsIgnoreCase("v1_8_R1") || !nmsVersion.startsWith("v1_8_")) && !nmsVersion.startsWith("v1_9_")) {
                c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatSerializer");
                c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                o = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
            } else {
                c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatComponentText");
                c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
                o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            }
            ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            Method m1 = c1.getDeclaredMethod("getHandle");
            Object h = m1.invoke(p);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
