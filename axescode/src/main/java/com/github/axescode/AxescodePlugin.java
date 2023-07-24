package com.github.axescode;

import com.github.axescode.command.CommandBuilder;
import com.github.axescode.listener.PlayerListener;
import com.github.axescode.listener.ServerListener;
import com.github.axescode.listener.UIListener;
import com.github.axescode.mybatis.HikariDataSourceFactory;
import com.github.axescode.mybatis.MybatisConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
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

        CommandBuilder.register(this, "tc", builder -> {
            builder.onExecute((player, args) -> {
                player.sendMessage(Component.text("gd"));
                return true;
            });
            builder.openNode("node1", node -> {
                node.addRequirement(player -> player.getGameMode() == GameMode.CREATIVE);

                node.onExecute((player, args) -> {
                    player.sendMessage(Component.text("node1"));
                    return true;
                });
            });
            builder.openNode("node2", node -> {
                node.addNode("n1", n1 -> {
                    n1.addNode("n2", n2 -> {});
                });
            });
            builder.openNode("node3", node -> {});
            builder.openNode("node4", node -> {});
        });
    }

    @Override
    public void onDisable() {
        plugin.getServer().getOnlinePlayers().forEach(Player::kick);
        HikariDataSourceFactory.closeDataSource();
    }

    private void registerAllListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public static void info(Object msg) {
        plugin.getLogger().info(msg.toString());
    }
    public static void warn(Object msg) {
        plugin.getLogger().warning(msg.toString());
    }
}
