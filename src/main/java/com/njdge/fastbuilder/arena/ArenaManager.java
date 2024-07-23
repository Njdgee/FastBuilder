package com.njdge.fastbuilder.arena;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.arena.impl.Island;
import com.njdge.fastbuilder.profile.PlayerProfile;
import jdk.nashorn.internal.ir.Block;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static com.njdge.fastbuilder.utils.WorldUtil.getOrCreateWorld;

public class ArenaManager {
    private FastBuilder plugin;
    private HashMap<String, Arena> arenas;

    public ArenaManager(FastBuilder plugin) {
        this.plugin = plugin;
        arenas = new HashMap<>();
    }

    public void loadArenas() {
        //short arena
        Arena shortArena = new Arena("Short");
        shortArena.setType(ArenaType.SHORT);
        World shortWorld = getOrCreateWorld("short");
        shortArena.setCenter(new Location(shortWorld, 0, 100, 0));
        shortArena.initArena();
        arenas.put("Short", shortArena);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public void join(Player player,Arena arena) {
        Island island = arena.getFreeIsland();
        if (island == null) {
            player.sendMessage("No free islands");
            return;
        }
        player.teleport(island.getSpawn());
        island.setPlayer(player);
        island.setEmpty(false);
        arena.getPlayers().put(player,null);
    }

    public void leave(Player player) {
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        profile.clearBlocks();
        Arena arena = profile.getArena();
        Island island = profile.getArena().getIsland(player);
        island.setPlayer(null);
        island.setEmpty(true);
        arena.getPlayers().remove(player);
    }
}
