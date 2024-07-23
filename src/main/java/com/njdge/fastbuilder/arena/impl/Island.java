package com.njdge.fastbuilder.arena.impl;

import com.njdge.fastbuilder.arena.Arena;
import com.njdge.fastbuilder.utils.Cuboid;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
public class Island {
    private int id;
    private Location spawn;
    private Cuboid cuboid, placeableCuboid;
    private Arena arena;
    private boolean empty = true;
    private Player player;

    public Island(Arena arena, int id) {
        this.arena = arena;
        this.id = id;
    }
}
