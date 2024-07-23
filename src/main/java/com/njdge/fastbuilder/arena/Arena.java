package com.njdge.fastbuilder.arena;

import com.njdge.fastbuilder.arena.impl.Island;
import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.Cuboid;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.njdge.fastbuilder.utils.TimeUtil.formatTime;

@Data
public class Arena {
    private String name;
    private ArenaType type;
    private Location center;
    private int deathHeight = 8;
    private int roomsAmount = 25;
    private HashMap<Integer, Island> rooms;
    private HashMap<Player, Long> players = new HashMap<>();

    public Arena(String name) {
        this.name = name;
    }

    public void initArena() {
        rooms = new HashMap<>();
        int haf = roomsAmount / 2;
        int width = 12; //the length between 2 rooms
        for (int i = 0; i < roomsAmount; i++) {
            int x = (i - haf) * width;
            Location loc = new Location(center.getWorld(), center.getX() + x + 0.5, center.getY(), center.getZ() + 0.5);
            Island island = new Island(this, i);
            island.setSpawn(loc);
            island.setCuboid(new Cuboid(loc.clone().add(-7, 100, -5), loc.clone().add(7, -deathHeight, type.getLength() + 8)));
            island.setPlaceableCuboid(new Cuboid(loc.clone().add(-4, 100, 3), loc.clone().add(4, -deathHeight, type.getLength() + 3)));
            rooms.put(i, island);
        }
    }

    public double getFinishLineZ() {
        return (center.getZ() + type.getLength() - 0.75);
    }

    public int getFinishLineY() {
        return (int) center.getY() + type.getHeight();
    }

    public int getDeathHeight() {
        return (int) center.getY() - deathHeight;
    }

    public Island getIsland(int id) {
        return rooms.get(id);
    }

    public Island getIsland(Player player) {
        for (Island island : rooms.values()) {
            if (island.getPlayer() != null && island.getPlayer().equals(player)) {
                return island;
            }
        }
        return null;
    }

    public Island getFreeIsland() {
        ArrayList<Island> freeIslands = new ArrayList<>();
        for (Island island : rooms.values()) {
            if (island.isEmpty()) {
                freeIslands.add(island);
            }
        }
        if (freeIslands.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            return freeIslands.get(rand.nextInt(freeIslands.size()));
        }
    }

    public String getLeader1() {
        Map.Entry<Player, Long> minEntry = players.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .min(Map.Entry.comparingByValue())
                .orElse(null);

        if (minEntry == null) {
            return CC.GRAY + "-.---";
        }

        String leader = minEntry.getKey().getName();

        String formattedTime = formatTime(minEntry.getValue());
        return CC.GREEN + leader + ": " + CC.YELLOW + formattedTime;
    }

    public String getLeader2() {
        List<Map.Entry<Player, Long>> sortedPlayers = players.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        if (sortedPlayers.size() < 2) {
            return CC.GRAY + "-.---";
        }

        String leader = sortedPlayers.get(1).getKey().getName();

        String formattedTime = formatTime(sortedPlayers.get(1).getValue());
        return CC.GREEN + leader + ": " + CC.YELLOW + formattedTime;
    }

    public String getLeader3() {
        List<Map.Entry<Player, Long>> sortedPlayers = players.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        if (sortedPlayers.size() < 3) {
            return CC.GRAY + "-.---";
        }

        String leader = sortedPlayers.get(2).getKey().getName();

        String formattedTime = formatTime(sortedPlayers.get(2).getValue());

        return CC.GREEN + leader + ": " + CC.YELLOW + formattedTime;
    }
}
