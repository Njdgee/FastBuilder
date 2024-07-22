package com.njdge.fastbuilder.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.njdge.fastbuilder.FastBuilder;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.ThreadFactory;

//Credit: https://github.com/diamond-rip/Eden/blob/master/src/main/java/rip/diamond/practice/util/Tasks.java

public class Tasks {

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static void run(Runnable runnable, boolean async) {
        if(async) {
            FastBuilder.getInstance().getServer().getScheduler().runTaskAsynchronously(FastBuilder.getInstance(), runnable);
        } else {
            runnable.run();
        }
    }

    public static void run(Runnable runnable) {
        FastBuilder.getInstance().getServer().getScheduler().runTask(FastBuilder.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        FastBuilder.getInstance().getServer().getScheduler().runTaskAsynchronously(FastBuilder.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, long delay) {
        FastBuilder.getInstance().getServer().getScheduler().runTaskLater(FastBuilder.getInstance(), runnable, delay);
    }

    public static void runAsyncLater(Runnable runnable, long delay) {
        FastBuilder.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(FastBuilder.getInstance(), runnable, delay);
    }

    public static void runTimer(Runnable runnable, long delay, long interval) {
        FastBuilder.getInstance().getServer().getScheduler().runTaskTimer(FastBuilder.getInstance(), runnable, delay, interval);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long interval) {
        FastBuilder.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(FastBuilder.getInstance(), runnable, delay, interval);
    }

    public static BukkitScheduler getScheduler() {
        return FastBuilder.getInstance().getServer().getScheduler();
    }
}