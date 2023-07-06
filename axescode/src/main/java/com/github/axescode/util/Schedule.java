package com.github.axescode.util;

import com.github.axescode.AxescodePlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Schedule {
    private static final BukkitScheduler scheduler = AxescodePlugin.inst().getServer().getScheduler();

    public static void sync(Long delay, Runnable block) { scheduler.runTaskLater(AxescodePlugin.inst(), block, delay); }
    public static void loop(Long delay, Long period, Runnable block) { scheduler.runTaskTimer(AxescodePlugin.inst(), block, delay, period); }
    public static void async(Long delay, Runnable block) { scheduler.runTaskLaterAsynchronously(AxescodePlugin.inst(), block, delay); }
    public static void asyncLoop(Long delay, Long period, Runnable block) { scheduler.runTaskTimerAsynchronously(AxescodePlugin.inst(), block, delay, period); }


}



