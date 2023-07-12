package com.github.axescode.front.listener;

import com.github.axescode.container.Containers;
import com.github.axescode.core.player.PlayerDAO;
import com.github.axescode.core.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        PlayerDAO.use(playerDAO -> {
            String playerName = e.getPlayerProfile().getName();
            PlayerData playerData = playerDAO.findPlayerByName(playerName);

            if(playerData == null) {
                playerDAO.fastSave(playerName);
                playerData = playerDAO.findPlayerByName(playerName);
                if(playerData == null) {
                    e.kickMessage(Component.text("플레이어 데이터를 불러오는 데에 실패했어요."));
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                }
            }

            Containers.getPlayerDataContainer().addData(playerName, playerData);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Containers.getPlayerDataContainer().removeData(e.getPlayer().getName());
    }
}
