package com.njdge.fastbuilder.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

//Credit: https://github.com/diamond-rip/Eden/blob/master/src/main/java/rip/diamond/practice/util/TitleSender.java

public class TitleSender {

    public static void sendTitle(Player player, String text, PacketPlayOutTitle.EnumTitleAction titleAction, int fadeInTime, int showTime, int fadeOutTime) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(text) + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(titleAction, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeInTime, showTime, fadeOutTime);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }
}