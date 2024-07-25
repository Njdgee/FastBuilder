package com.njdge.fastbuilder.profile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.njdge.fastbuilder.FastBuilder;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Data
public class ProfileManager {
    private HashMap<UUID, PlayerProfile> profiles;
    private FastBuilder plugin;
    private final MongoDatabase database;

    public ProfileManager(FastBuilder plugin) {
        profiles = new HashMap<>();
        this.plugin = plugin;
        this.database = plugin.getMongoManager().getDatabase();
        createCollection();
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
        database.getCollection("fastbuilder_profiles").insertOne(new Document(profile.toDoc()));
    }

    public void load(PlayerProfile profile) {
        UUID uuid = profile.getUuid();
        if (plugin.getMongoManager().isEnabled()) {
                Document document = database.getCollection("fastbuilder_profiles").find(Filters.eq("uuid", uuid.toString())).first();
                importFromBson(document, profile);
        }else {
            System.out.println(4);
            profile.setName(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".name"));
            profile.setBlockType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".blockType")));
            profile.setPickaxeType(Material.valueOf(plugin.getProfileConfig().getConfiguration().getString(profile.getUuid().toString() + ".pickaxeType")));
            profile.setPb(plugin.getProfileConfig().getConfiguration().getLong(profile.getUuid().toString() + ".pb"));
        }
    }

    public void save(PlayerProfile profile) {
        if (plugin.getMongoManager().isEnabled()) {
            database.getCollection("fastbuilder_profiles").replaceOne(Filters.eq("uuid", profile.getUuid().toString()), profile.toDoc());
        }else {
            plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".name", profile.getName());
            plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".blockType", profile.getBlockType().name());
            plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pickaxeType", profile.getPickaxeType().name());
            plugin.getProfileConfig().getConfiguration().set(profile.getUuid().toString() + ".pb", profile.getPb());
            plugin.getProfileConfig().save();
        }
    }
    public void createCollection() {

        boolean collectionExists = database.listCollectionNames()
                .into(new ArrayList<>())
                .contains("fastbuilder_profiles");

        if (!collectionExists) {
            database.createCollection("fastbuilder_profiles");
        }

    }
    public boolean loginBefore(UUID uuid) {
        if (plugin.getMongoManager().isEnabled()) {
            return database.getCollection("fastbuilder_profiles").find(Filters.eq("uuid", uuid.toString())).first() != null;
        }else {
            if (plugin.getProfileConfig() == null || plugin.getProfileConfig().getConfiguration() == null) {
                return false;
            }
            return plugin.getProfileConfig().getConfiguration().contains(uuid.toString());
        }
    }

    public void importFromBson(Document document, PlayerProfile profile) {
        profile.setName(document.getString("name"));
        profile.setBlockType(Material.valueOf(document.getString("blockType")));
        profile.setPickaxeType(Material.valueOf(document.getString("pickaxeType")));
        profile.setPb(document.getLong("pb"));
    }
}

