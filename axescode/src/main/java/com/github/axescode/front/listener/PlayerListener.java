package com.github.axescode.front.listener;

import com.github.axescode.container.Containers;
import com.github.axescode.core.player.PlayerDAO;
import com.github.axescode.core.player.PlayerData;
import com.github.axescode.core.player.PlayerVO;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.github.axescode.core.player.PlayerDAO.toData;

public class PlayerListener implements Listener {
    private final Component failedMessage = Component.text("플레이어 데이터를 불러오는 데에 실패했어요.");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        try {
            PlayerDAO.use(playerDAO -> {
                String playerName = e.getPlayerProfile().getName();
                PlayerVO playerVO = playerDAO.findPlayerByName(playerName);

                if(playerVO == null) {
                    playerVO = PlayerVO.builder()
                            .playerMoney(0L)
                            .playerNickName("")
                            .playerProficiencyStack(0)
                            .playerJobType("NONE")
                            .playerName(playerName)
                            .build();
                    playerDAO.save(playerVO);
                }

                Containers.getPlayerDataContainer().addData(playerName, toData(playerVO));
            });
        } catch(Exception ex) {
            ex.printStackTrace();
            e.kickMessage(failedMessage);
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
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
