package com.njdge.fastbuilder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class WeatherManager {
    private FastBuilder plugin;

    public WeatherManager(FastBuilder plugin) {
        this.plugin = plugin;
        keepWeatherClear();
    }

    public void keepWeatherClear() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    world.setStorm(false);
                    world.setThundering(false);
                }
            }
        }.runTaskTimer(plugin, 0L, 6000L);
    }
}