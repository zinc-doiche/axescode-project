package com.github.axescode;

import com.github.axescode.listener.PlayerListener;
import com.github.axescode.listener.ServerListener;
import com.github.axescode.listener.UIListener;
import com.github.axescode.mybatis.MybatisConfig;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
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
                new ServerListener(),
                new PlayerListener(),
                new UIListener()
        );
    }

    @Override
    public void onDisable() {
        plugin.getServer().getOnlinePlayers().forEach(Player::kick);
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
    public static void warn(Object msg) {
        plugin.getLogger().warning(msg.toString());
    }
}
