package com.njdge.fastbuilder.adapters;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.arena.Arena;
import com.njdge.fastbuilder.profile.PlayerProfile;
import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.assemble.AssembleAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ScoreboardAdapter implements AssembleAdapter {
    private final FastBuilder plugin;

    public ScoreboardAdapter(FastBuilder plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "McPlayHD.net";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>(this.fetchLines(player));
        return lines.stream()
                .map(line -> line
                        .replace("%splitter%", "┃")
                        .replace("|", "┃"))
                .collect(Collectors.toList());
    }

    private List<String> fetchLines(Player player) {
        PlayerProfile profile = plugin.getProfileManager().get(player.getUniqueId());
        if (profile == null) return new ArrayList<>();
        switch (profile.getState()) {
            case PLAYING: {
                return this.getPlayingLines(player);
            }
        }
        return new ArrayList<>();
    }

    private List<String> getPlayingLines(Player player) {
        PlayerProfile profile = plugin.getProfileManager().get(player.getUniqueId());
        Arena arena = profile.getArena();
        String leader1 = arena.getLeader1();
        String leader2 = arena.getLeader2();
        String leader3 = arena.getLeader3();
        String pb = profile.getPBString();
        String blocks = String.valueOf(profile.getBlocks() == 0 ? CC.GRAY + "-" : profile.getBlocks());
        String time = profile.getTimeString();

        List<String> lines = new ArrayList<>();
        lines.add(CC.translate("&8&l│"));
        lines.add(CC.translate("&8&l├ &b&lPersonal Best&7&l:"));
        lines.add(CC.translate("&8&l│  &e" + pb));
        lines.add(CC.translate("&8&l│"));
        lines.add(CC.translate("&8&l├ &e&lSession Top3&7:"));
        lines.add(CC.translate("&8&l│  " + leader1));
        lines.add(CC.translate("&8&l│  " + leader2));
        lines.add(CC.translate("&8&l│  " + leader3));
        lines.add(CC.translate("&8&l│"));
        lines.add(CC.translate("&8&l├ &3&lBlocks&7&l:"));
        lines.add(CC.translate("&8&l│  &e" + blocks));
        lines.add(CC.translate("&8&l│"));
        lines.add(CC.translate("&8&l├ &a&lTime&7&l:"));
        lines.add(CC.translate("&8&l│  &e" + time));
        lines.add(CC.translate("&8&l└─&6&oMcPlayHD.net&8&l─"));


        return lines;
    }
}
