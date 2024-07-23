package com.njdge.fastbuilder.profile;

import org.bukkit.Material;
import org.bukkit.block.Block;

import static com.njdge.fastbuilder.utils.Tasks.runLater;

public class BlockClearAnimation {
    public static void sequential(PlayerProfile profile) {
        int totalBlocks = profile.getPlacedBlocks().size();
        int[] counter = {0};
        profile.getPlacedBlocks().forEach(location -> {
            Block block = location.getBlock();
            runLater(() -> block.setType(Material.AIR), (long) (totalBlocks * ((double) counter[0]++ / totalBlocks)));
        });
    }

    public static void clear(PlayerProfile profile) {
        profile.getPlacedBlocks().forEach(location -> {
            Block block = location.getBlock();
            runLater(() -> block.setType(Material.AIR), 0);
        });
    }
}
