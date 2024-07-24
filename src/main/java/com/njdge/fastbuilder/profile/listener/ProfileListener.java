package com.njdge.fastbuilder.profile.listener;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.arena.Arena;
import com.njdge.fastbuilder.arena.impl.Island;
import com.njdge.fastbuilder.profile.PlayerProfile;
import com.njdge.fastbuilder.profile.ProfileState;
import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.TitleSender;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

import static com.njdge.fastbuilder.utils.ActionBar.sendActionBar;
import static com.njdge.fastbuilder.utils.Tasks.runLater;
import static com.njdge.fastbuilder.utils.Util.*;

public class ProfileListener implements Listener {
    private FastBuilder plugin;

    public ProfileListener(FastBuilder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {

        String name = e.getName();
        UUID uuid = e.getUniqueId();

        if (name == null || uuid == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "An error occurred while logging in");
            return;
        }

        if (plugin.getProfileManager().getProfiles().containsKey(uuid)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "You are already logged in, please rejoin");
            plugin.getProfileManager().logout(uuid);
            return;
        }

        PlayerProfile profile = plugin.getProfileManager().login(name, uuid);
        profile.setState(ProfileState.PLAYING);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        profile.setPlayer(player);
        plugin.getArenaManager().join(player, profile.getArena());
        profile.reset();

        profile.giveItems();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        Arena arena = profile.getArena();
        Island island = arena.getIsland(player);
        if (profile.getState().equals(ProfileState.PLAYING)) {
            //death height
            if (player.getLocation().getY() < arena.getDeathHeight()) {
                player.teleport(island.getSpawn());
                profile.clearBlocks();
                profile.reset();
                profile.giveItems();
                profile.stopTimer();
                profile.setPlaced(false);
            }

            //Cuboid
            if (!island.getCuboid().isIn(player.getLocation())) {
                player.teleport(island.getSpawn());
                profile.clearBlocks();
                profile.reset();
                profile.giveItems();
                profile.stopTimer();
                profile.setPlaced(false);
            }

            //finish
            if (player.getLocation().getBlockZ() >= arena.getFinishLineZ() && player.getLocation().getY() == arena.getFinishLineY() && !profile.isFinished()) {
                profile.stopTimer();
                profile.setFinished(true);
                Long time = profile.getTime();
                Long pb = profile.getPb();
                Long arenaTime = arena.getPlayers().get(player);
                String timeString = profile.getTimeString();
                int emeralds = 16;
                boolean isPb = false;


                profile.clearBlocks();
                //leaderboard
                if (arena.getPlayers().get(player) == null) {
                    arena.getPlayers().put(player, profile.getTime());
                } else {
                    if (profile.getTime() < arena.getPlayers().get(player)) {
                        arena.getPlayers().replace(player, profile.getTime());
                    }
                }
                //pb
                if (profile.getPb() == null) {
                    profile.setPb(profile.getTime());
                    isPb = true;
                } else {
                    if (profile.getTime() < profile.getPb()) {
                        profile.setPb(profile.getTime());
                        isPb = true;
                    }
                }

                player.sendMessage(CC.translate("&8&l▬&7&l▬▬▬&8&l▬&7&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬&8&l▬&7&l▬▬▬&7&l▬&8&l▬"));
                player.sendMessage("");
                player.sendMessage(CC.translate("       &f&lFastBuilder"));
                player.sendMessage("");
                player.sendMessage(CC.translate("       &aYou made it to the &3&lfinish line&a."));
                player.sendMessage("");
                if (profile.getPb() != null) {
                    player.sendMessage(CC.translate("       &ePrevious &bbest&7: " + renderPreviousBest(pb, time)));
                }
                player.sendMessage(CC.translate("       &eTime &btaken&7: " + renderTimeTaken(arenaTime, time)));
                if (isPb) {
                    player.sendMessage(CC.translate("       &a&lNEW PERSONAL BEST!"));
                }
                player.sendMessage("");
                player.sendMessage(CC.translate("&8&l▬&7&l▬&8&l▬▬&7&l▬▬▬▬&8&l▬▬&7&l▬&8&l▬▬▬▬▬▬&7&l▬&8&l▬&7&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬&8&l▬&7&l▬&8&l▬▬▬▬▬&7&l▬&8&l▬▬&7&l▬▬▬▬&8&l▬▬&7&l▬&8&l▬"));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                TitleSender.sendTitle(player, CC.GREEN + "Time" + CC.GRAY + ":" + CC.YELLOW + timeString, PacketPlayOutTitle.EnumTitleAction.TITLE, 1, 20, 1);
                TitleSender.sendTitle(player, CC.GREEN + CC.BOLD + "+" + CC.WHITE + CC.BOLD + emeralds + CC.DARK_GREEN + CC.BOLD + "Em" + CC.GREEN + CC.BOLD + "er" + CC.DARK_GREEN + CC.BOLD + "al" + CC.GREEN + CC.BOLD + "ds", PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 1, 20, 1);


                runLater(() -> {
                    player.teleport(island.getSpawn());
                    profile.setFinished(false);
                    profile.setPlaced(false);
                    profile.reset();
                    profile.giveItems();
                }, 40);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        Arena arena = profile.getArena();
        Island island = arena.getIsland(player);



        if (profile.getState().equals(ProfileState.PLAYING)) {
            if (!island.getPlaceableCuboid().isIn(e.getBlock().getLocation())) {
                e.setCancelled(true);
                sendActionBar(player, CC.RED + "You can't place blocks here");
            } else {
                if (!profile.isPlaced()) {
                    profile.setBlocks(0);
                    profile.setPlaced(true);
                    profile.startTimer();
                }
                //Fix scoreboard blocks still adding even if placing on "sus place" - bedtwL 07/24/2024
                profile.setBlocks(profile.getBlocks() + 1);
                profile.getPlacedBlocks().add(e.getBlock().getLocation());
            }
        } else if (profile.getState().equals(ProfileState.EDITING)) {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        if (profile.getState().equals(ProfileState.PLAYING)) {
            if (!profile.getPlacedBlocks().contains(e.getBlock().getLocation())) {
                e.setCancelled(true);
                sendActionBar(player, CC.RED + "You can't break blocks here");
            } else {
                profile.getPlacedBlocks().remove(e.getBlock().getLocation());
                profile.setBlocks(profile.getBlocks() - 1);
            }
        } else if (profile.getState().equals(ProfileState.EDITING)) {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        if (profile.getState().equals(ProfileState.PLAYING)) {
            e.setCancelled(true);
        } else if (profile.getState().equals(ProfileState.EDITING)) {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
        if (profile.getState().equals(ProfileState.PLAYING)) {
            e.setCancelled(true);
        } else if (profile.getState().equals(ProfileState.EDITING)) {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
            if (profile.getState().equals(ProfileState.PLAYING)) {
                e.setCancelled(true);
            } else if (profile.getState().equals(ProfileState.EDITING)) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            PlayerProfile profile = plugin.getProfileManager().getProfiles().get(player.getUniqueId());
            if (profile.getState().equals(ProfileState.PLAYING)) {
                e.setCancelled(true);
            } else if (profile.getState().equals(ProfileState.EDITING)) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.getArenaManager().leave(player);
        plugin.getProfileManager().logout(uuid);
    }
}
