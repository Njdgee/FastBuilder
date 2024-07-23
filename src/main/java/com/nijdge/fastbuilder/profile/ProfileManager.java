package com.njdge.fastbuilder.profile;

import com.njdge.fastbuilder.FastBuilder;
import com.njdge.fastbuilder.utils.BasicConfigFile;
import lombok.Data;
import me.bedtwL.fb.utils.DataUtils;
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
        // Default Code (Not Using MariaDB)
        /*
        profile.setName(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".name"));
        profile.setBlockType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".blockType")));
        profile.setPickaxeType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".pickaxeType")));
        profile.setPb(plugin.getProfileConfig().getConfiguration().getLong(profile.getUuid().toString() + ".pb"));
        */
        //MariaDB Code:
        profile.setName(DataUtils.getStringValue(profile.getUuid(),"name"));
        profile.setBlockType(Material.valueOf(DataUtils.getStringValue(profile.getUuid(),"blockType")));
        profile.setPickaxeType(Material.valueOf(DataUtils.getStringValue(profile.getUuid(),"pickaxeType")));
        profile.setPb(DataUtils.getPb(profile.getUuid()));
    }

    public void save(PlayerProfile profile) {
        // Default Code (Not Using MariaDB)
        /*
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".name", profile.getName());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".blockType", profile.getBlockType().name());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pickaxeType", profile.getPickaxeType().name());
        plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pb", profile.getPb());
        plugin.getProfileConfig().save();
        */
        //MariaDB Code:
        DataUtils.setStringValue(profile.getUuid(),"name",profile.getName());
        DataUtils.setStringValue(profile.getUuid(),"blockType",profile.getBlockType().name());
        DataUtils.setStringValue(profile.getUuid(),"pickaxeType",profile.getPickaxeType().name());
        DataUtils.setPb(profile.getUuid(),profile.getPb());
    }

    public boolean loginBefore(UUID uuid) {
        //Default Code
        /*
        if (plugin.getProfileConfig() == null || plugin.getProfileConfig().getConfiguration() == null) {
            return false;
        }
        return plugin.getProfileConfig().getConfiguration().contains(uuid.toString());
        */
        //MariaDB Code:
        return (DataUtils.getStringValue(uuid,"name")!="");
    }
}
