package com.github.axescode.front.listener;

import com.github.axescode.container.Containers;
import com.github.axescode.core.player.PlayerDAO;
import com.github.axescode.core.player.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerDAO.use(playerDAO ->
                playerDAO.findPlayerByName(e.getPlayer().getName())
                    .ifPresentOrElse(playerData -> Containers.getPlayerDataContainer().addData(playerData.getPlayerName(), playerData),
                            () -> playerDAO.fastSave(e.getPlayer().getName())));

        //Containers.getPlayerDataContainer().getData(e.getPlayer().getName())
                //.map(PlayerData::toString).ifPresent(e.getPlayer()::sendMessage);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Containers.getPlayerDataContainer().removeData(e.getPlayer().getName());
    }


//    @EventHandler
//    public void onCrop(BlockBreakEvent e) {
//        e.getPlayer().sendMessage(e.getBlock().toString());
//
//        Containers.getPlayerDataContainer().getData(e.getPlayer().getName()).ifPresent(playerData -> {
//            switch (playerData.getPlayerJob().getJobType()) {
//                case NONE -> {;}
//                case FARMER -> {
//
//                    //add some constraint preventing non-crops from triggering this event
//
//                    ((FarmerAbility) playerData.getPlayerJob().getAbility()).onCrop(e, playerData);
//                }
//                case MINER -> {;}
//                case FISHER -> {;}
//            }
//        });
//    }
}
