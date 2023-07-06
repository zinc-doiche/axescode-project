package com.github.axescode;

import com.github.axescode.front.listener.PluginListener;
import com.github.axescode.mybatis.MybatisConfig;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

public class AxescodePlugin extends JavaPlugin {
    private static AxescodePlugin plugin;

    public static JavaPlugin inst() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        MybatisConfig.init();

        registerAllListeners(
                new PluginListener()
        );
    }

    private void registerAllListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public static void register(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = Objects.requireNonNull(plugin.getCommand(command));
        pluginCommand.setExecutor(executor);
    }

    public static void info(Object msg) {
        plugin.getLogger().info(msg.toString());
    }
}
