package com.njdge.fastbuilder;

import com.njdge.fastbuilder.adapters.ScoreboardAdapter;
import com.njdge.fastbuilder.arena.ArenaManager;
import com.njdge.fastbuilder.profile.ProfileManager;
import com.njdge.fastbuilder.profile.command.EditCommand;
import com.njdge.fastbuilder.profile.listener.ProfileListener;
import com.njdge.fastbuilder.utils.BasicConfigFile;
import com.njdge.fastbuilder.utils.assemble.Assemble;
import com.njdge.fastbuilder.utils.assemble.AssembleStyle;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter

public final class FastBuilder extends JavaPlugin {
    @Getter
    public static FastBuilder instance;
    private ArenaManager arenaManager;
    private ProfileManager profileManager;
    private WeatherManager weatherManager;

    private BasicConfigFile profileConfig;


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
        this.arenaManager = new ArenaManager(this);
        this.profileManager = new ProfileManager(this);
        this.weatherManager = new WeatherManager(this);
    }

    void loadGeneral() {
        arenaManager.loadArenas();
    }

    void loadConfig() {
        this.profileConfig = new BasicConfigFile(this, "profile.yml");
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
