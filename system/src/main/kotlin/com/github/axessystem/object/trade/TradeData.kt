package com.github.axessystem.`object`.trade

import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.core.trade.TradeVO
import com.github.axessystem.pluginScope
import com.github.axessystem.ui.TradeUI
import com.github.axessystem.util.useOutputStream
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.async
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashSet

data class TradeData(
    val acceptor: Trader,
    val requester: Trader
) {
    var tradeState = TradeState.PROCESSING
        private set

    fun startTrade() {
        pluginScope.async {
            acceptor.playerData.playerEntity.openFrame(TradeUI.getFrame(this@TradeData, acceptor))
            requester.playerData.playerEntity.openFrame(TradeUI.getFrame(this@TradeData, requester))
        }
    }

    fun saveData() {
        TradeDAO.use { dao ->
            TradeVO.builder()
                .tradeAcceptorId(acceptor.playerData.playerId)
                .tradeRequesterId(requester.playerData.playerId)
                .tradeAcceptorSentMoney(acceptor.tradeMoney)
                .tradeRequesterSentMoney(requester.tradeMoney)
                .build()
            .let { tradeVO ->
                //DB 저장
                dao.save(tradeVO)

                //ItemStack 직렬화
                useOutputStream { bs, os ->
                    acceptor.tradeItems.map { item ->
                        os.writeObject(item.serialize().apply {
                            if(containsKey("meta")) this["meta"] = (this["meta"] as ItemMeta).serialize()
                        })
                        os.flush()
                        TradeItemVO.builder()
                            .tradeId(tradeVO.tradeId)
                            .playerId(acceptor.playerData.playerId)
                            .tradeItem(Base64.getEncoder().encodeToString(bs.toByteArray()))
                            .build()
                    }.forEach(dao::saveItem) // 각각 저장
                }
            }
        }
    }

    fun deny() {
        tradeState = TradeState.DENIED
    }

    fun success() {
        tradeState = TradeState.SUCCESS
    }

}

data class Trader(
    val playerData: PlayerData,
    var tradeMoney: Long = 0
) {
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)
    var isFull = false
}

enum class TradeState {
    PROCESSING,
    DENIED,
    SUCCESS
    ;
}