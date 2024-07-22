package com.njdge.fastbuilder.profile;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.utils.BasicConfigFile;
import lombok.Data;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.UUID;
@Data
public class ProfileManager {
    private HashMap<UUID, PlayerProfile> profiles;
    private FastBuilder plugin;

    public ProfileManager(FastBuilder plugin) {
        profiles = new HashMap<>();
        this.plugin = plugin;
    }

    public PlayerProfile login(String name, UUID uuid) {
        PlayerProfile profile = new PlayerProfile(uuid);
        profile.setName(name);
        profile.setArena(plugin.getArenaManager().getArena("Short"));
        if (loginBefore(uuid)) {
            load(profile);
        } else {
            loadDefault(profile);
        }
        profiles.put(uuid, profile);
        return profile;
    }

    public void logout(UUID uuid) {
        PlayerProfile profile = profiles.get(uuid);
        save(profile);
        profiles.remove(uuid);
    }

    public PlayerProfile get(UUID uuid) {
        return profiles.get(uuid);
    }

    public void loadDefault(PlayerProfile profile) {
        profile.setBlockType(Material.SANDSTONE);
        profile.setPickaxeType(Material.WOOD_PICKAXE);
        profile.setPb(null);
    }

    public void load(PlayerProfile profile) {
        profile.setName(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".name"));
        profile.setBlockType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".blockType")));
        profile.setPickaxeType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".pickaxeType")));
        profile.setPb(plugin.getProfileConfig().getConfiguration().getLong(profile.getUuid().toString() + ".pb"));
    }

    public void save(PlayerProfile profile) {
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".name", profile.getName());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".blockType", profile.getBlockType().name());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pickaxeType", profile.getPickaxeType().name());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pb", profile.getPb());
        plugin.getProfileConfig().save();
    }

    public boolean loginBefore(UUID uuid) {
        if (plugin.getProfileConfig() == null || plugin.getProfileConfig().getConfiguration() == null) {
            return false;
        }
        return plugin.getProfileConfig().getConfiguration().contains(uuid.toString());
    }
}
