package com.njdge.fastbuilder.profile.command;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.profile.PlayerProfile;
import com.njdge.fastbuilder.profile.ProfileState;
import com.njdge.fastbuilder.utils.CC;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class EditCommand implements CommandExecutor {
    private FastBuilder plugin;
    public EditCommand(FastBuilder plugin) {
        this.plugin = plugin;
        PluginCommand cmd = plugin.getCommand("edit");
        cmd.setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("fastbuilder.edit")){
                PlayerProfile profile = plugin.getProfileManager().get(player.getUniqueId());
                if (profile.getState().equals(ProfileState.EDITING)){
                    profile.setState(ProfileState.PLAYING);
                    player.sendMessage(CC.GREEN + "Edit Mode: " + CC.RED + "DISABLE");
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                } else {
                    profile.setState(ProfileState.EDITING);
                    player.sendMessage(CC.GREEN + "Edit Mode: " + CC.YELLOW + "ENABLE");
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                }
            }else {
                player.sendMessage(CC.RED + "You don't have permission to use this command");
            }

        }
        return false;
    }
}
