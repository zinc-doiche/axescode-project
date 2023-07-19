package com.github.axescode.listener;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.container.Containers;
import com.github.axescode.core.player.PlayerDAO;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerListener implements Listener {
    @EventHandler
    public void onLoad(ServerLoadEvent e) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AxescodePlugin.inst(), task -> {
            if(!Containers.getPlayerDataContainer().isEmpty()) {
                final long start = System.currentTimeMillis();

                AxescodePlugin.inst().getServer().broadcast(Component.text("플레이어 정보 저장 중..."));
                PlayerDAO.useTransaction(dao -> Containers.getPlayerDataContainer().getAll().stream()
                        .map(PlayerDAO::toVO).forEach(dao::modify));
                AxescodePlugin.inst().getServer().broadcast(
                        Component.text("저장 완료! (" + (System.currentTimeMillis() - start) + "ms 소요됨)"));
            }
        }, 0, 20L * 60L * 30L);
    }
}
