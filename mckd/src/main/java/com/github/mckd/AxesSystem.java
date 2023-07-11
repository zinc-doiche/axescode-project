package com.github.mckd;

import com.github.mckd.listener.UIListener;
import com.github.mckd.ui.UITemplates;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class AxesSystem extends JavaPlugin {
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        UITemplates.init();

        registerAll(
                new UIListener()
        );
    }

    public static JavaPlugin inst() {
        return plugin;
    }

    private void registerAll(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public static void info(Object msg) {
        plugin.getLogger().info(msg.toString());
    }

    public static void warn(Object msg) {
        plugin.getLogger().warning(msg.toString());
    }
}
