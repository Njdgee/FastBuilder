package com.njdge.fastbuilder;

import com.njdge.fastbuilder.arena.Arena;
import com.njdge.fastbuilder.arena.ArenaType;
import org.bukkit.Location;

public class test {
    public static void main(String[] args) {
        Arena shortArena = new Arena("Short");
        shortArena.setType(ArenaType.SHORT);
        shortArena.setCenter(new Location(null, 0, 100, 0));
        shortArena.initArena();
        for (int i = 0; i < shortArena.getRoomsAmount(); i++) {
            System.out.println("Room " + i + " is at " + shortArena.getIsland(i).getSpawn());
        }
    }
}

