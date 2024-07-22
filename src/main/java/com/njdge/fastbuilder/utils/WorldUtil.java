package com.njdge.fastbuilder.utils;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldUtil {
    public static World getOrCreateWorld(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            WorldCreator creator = new WorldCreator(worldName);
            world = Bukkit.getServer().createWorld(creator);
        }
        return world;
    }
}
