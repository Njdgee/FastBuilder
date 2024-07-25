package com.njdge.fastbuilder.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.njdge.fastbuilder.FastBuilder;
import lombok.Getter;
import org.bson.Document;
//Credit: https://github.com/diamond-rip/Eden/blob/master/src/main/java/rip/diamond/practice/database/MongoManager.java
@Getter
public class MongoManager {

    private final FastBuilder plugin;

    private boolean enabled;
    private MongoDatabase database;
    private MongoClient client;
    @Getter private MongoCollection<Document> profiles;

    public MongoManager(FastBuilder plugin) {
        this.plugin = plugin;
        this.init();
        this.enabled = plugin.getConfig().getConfiguration().getBoolean("mongo.enabled");
    }

    public void init() {
        String uri = plugin.getConfig().getConfiguration().getString("mongo.uri");
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase(plugin.getConfig().getConfiguration().getString("mongo.database"));
        this.loadCollections();
    }

    public void loadCollections() {
        profiles = this.database.getCollection("fastbuilder_profiles");
    }
}