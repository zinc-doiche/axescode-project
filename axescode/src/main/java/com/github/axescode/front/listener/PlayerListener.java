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

public class PlayerListener implements Listener {
    private final Component failedMessage = Component.text("플레이어 데이터를 불러오는 데에 실패했어요.");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        PlayerDAO.use(playerDAO -> {
            String playerName = e.getPlayerProfile().getName();
            PlayerData playerData = playerDAO.findPlayerByName(playerName);

            if(playerData == null) {
                playerDAO.fastSave(playerName);
                playerData = playerDAO.findPlayerByName(playerName);
                if(playerData == null) {
                    e.kickMessage(failedMessage);
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                }
            }

            Containers.getPlayerDataContainer().addData(playerName, playerData);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        PlayerData playerData = Containers.getPlayerDataContainer().getData(e.getPlayer().getName());
        if(playerData == null) {
            e.getPlayer().kick(failedMessage);
            return;
        }
        playerData.setPlayerEntity(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        PlayerDAO.use(playerDAO -> {
            PlayerData playerData = Containers.getPlayerDataContainer().getData(e.getPlayer().getName());
            playerDAO.modify(PlayerDAO.toVO(playerData));
        });
        Containers.getPlayerDataContainer().removeData(e.getPlayer().getName());
    }
}
