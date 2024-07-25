package com.njdge.fastbuilder;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.njdge.fastbuilder.adapters.ScoreboardAdapter;
import com.njdge.fastbuilder.arena.ArenaManager;
import com.njdge.fastbuilder.database.MongoManager;
import com.njdge.fastbuilder.profile.ProfileManager;
import com.njdge.fastbuilder.profile.command.EditCommand;
import com.njdge.fastbuilder.profile.listener.ProfileListener;
import com.njdge.fastbuilder.utils.BasicConfigFile;
import com.njdge.fastbuilder.utils.assemble.Assemble;
import com.njdge.fastbuilder.utils.assemble.AssembleStyle;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

@Getter
@Setter
public final class FastBuilder extends JavaPlugin {
    @Getter
    public static FastBuilder instance;
    private ArenaManager arenaManager;
    private ProfileManager profileManager;
    private WeatherManager weatherManager;
    private MongoManager mongoManager;
    private BasicConfigFile profileConfig;
    private BasicConfigFile configFile;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        loadManagers();
        loadGeneral();
        loadListener();
        loadCommand();
        loadScoreboard();
    }

    @Override
    public void onDisable() {

    }

    void loadManagers() {
        this.mongoManager = new MongoManager(this);
        this.arenaManager = new ArenaManager(this);
        this.profileManager = new ProfileManager(this);
        this.weatherManager = new WeatherManager(this);

    }

    void loadGeneral() {
        arenaManager.loadArenas();
    }

    void loadConfig() {
        this.profileConfig = new BasicConfigFile(this, "profile.yml");
        this.configFile = new BasicConfigFile(this, "config.yml");
    }

    void loadCommand() {
        new EditCommand(this);
    }

    public void loadScoreboard() {
        ScoreboardAdapter scoreboardAdapter = new ScoreboardAdapter(this);
        Assemble assemble = new Assemble(this, scoreboardAdapter);
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.CUSTOM);
    }



    void loadListener() {
        Bukkit.getPluginManager().registerEvents(new ProfileListener(this), this);
    }
}
