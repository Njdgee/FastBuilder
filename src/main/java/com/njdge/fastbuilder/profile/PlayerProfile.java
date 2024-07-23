package com.njdge.fastbuilder.profile;

import com.njdge.fastbuilder.arena.Arena;
import com.njdge.fastbuilder.utils.PlayerUtils;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.njdge.fastbuilder.FastBuilderItems.*;
import static com.njdge.fastbuilder.FastBuilderItems.shop;
import static com.njdge.fastbuilder.profile.BlockClearAnimation.sequential;
import static com.njdge.fastbuilder.utils.TimeUtil.formatTime;

@Data
public class PlayerProfile {
    private UUID uuid;
    private Player player;
    private String name;
    private ProfileState state;
    private Material blockType , pickaxeType;
    private Arena arena;
    private boolean finished = false;
    private boolean placed = false;
    private List<Location> placedBlocks;
    private Long time,pb;
    private int blocks;
    private TimerTicker timer;


    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.time = 0L;
        this.blocks = 0;
        this.placedBlocks = new ArrayList<>();
        this.blockType = Material.SANDSTONE;
        this.pickaxeType = Material.WOOD_PICKAXE;
    }

    public void clearBlocks() {
        if (placedBlocks.isEmpty()) return;
        sequential(this);
        placedBlocks.clear();
    }

    public void reset() {
        PlayerUtils.reset(player,true);
    }

    public void giveItems() {
        player.getInventory().setItem(0, block.type(this.getBlockType()).build());
        player.getInventory().setItem(1, block.type(this.getBlockType()).build());
        player.getInventory().setItem(2, pickaxe.type(this.getPickaxeType()).build());
        player.getInventory().setItem(7, replay.build());
        player.getInventory().setItem(8, shop.build());
    }

    public String getTimeString() {
        return formatTime(this.time);
    }

    public String getPBString() {
        return formatTime(this.pb);
    }

    public void startTimer() {
        this.time = 0L;
        TimerTicker timer = new TimerTicker(0, 1, false,this);
        this.timer = timer;
    }

    public void stopTimer() {
        if (this.timer == null) {
            return;
        }
        this.timer.stop();
    }

    public Document toDoc() {
        Document doc = new Document()
                .append("uuid", uuid.toString())
                .append("name", name)
                .append("state", state.name())
                .append("blockType", blockType.name())
                .append("pickaxeType", pickaxeType.name())
                .append("pb", pb)
                .append("time", time)
                .append("blocks", blocks);
        return doc;
    }
}
