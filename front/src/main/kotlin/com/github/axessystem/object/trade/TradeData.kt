package com.github.axessystem.`object`.trade

import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.core.trade.TradeVO
import com.github.axescode.util.Items
import com.github.axescode.util.Items.AIR
import com.github.axescode.util.Items.isNullOrAir
import com.github.axessystem.pluginScope
import com.github.axessystem.ui.TradeUI
import com.github.axessystem.util.encodedItem
import com.github.axessystem.util.useOutputStream
import com.github.axessystem.util.writeItem
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.async
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

data class TradeData(
    val acceptor: Trader,
    val requester: Trader
) {
    var tradeState = TradeState.PROCEED
        private set

    fun startTrade() {
        pluginScope.async {
            acceptor.uiFrame = TradeUI(this@TradeData, acceptor)
            requester.uiFrame = TradeUI(this@TradeData, requester)

            openAll()
//            requester.openUI()
        }
    }

    /**
     * 거래상태를 [TradeState.DENIED]로 바꾸고 거래를 종료합니다.
     */
    fun deny() {
        tradeState = TradeState.DENIED
        closeAll()
    }

    /**
     * 거래상태를 [TradeState.SUCCESS]로 바꾸고 거래를 종료합니다.
     */
    fun success() {
        tradeState = TradeState.SUCCESS
        closeAll()
    }

    fun end() {
        tradeState = TradeState.END
    }

    fun openAll() {
        acceptor.openUI()
        requester.openUI()
    }

    fun closeAll() {
        acceptor.closeUI()
        requester.closeUI()
    }

    fun sendMessageAll(msg: String) {
        acceptor.sendMessage(msg)
        requester.sendMessage(msg)
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
                    acceptor.getItems().map { item ->
                        os.writeItem(item)
                        TradeItemVO.builder()
                            .tradeId(tradeVO.tradeId)
                            .playerId(acceptor.playerData.playerId)
                            .tradeItem(bs.encodedItem)
                            .build()
                    }.forEach(dao::saveItem) // 각각 저장
                }
            }
        }
    }
}

data class Trader(
    val playerData: PlayerData,
    var tradeMoney: Long = 0
) {
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)
    var uiFrame: TradeUI? = null

    val player: Player
        get() = playerData.playerEntity

    /**
     * Inventory 컬렉션화에서의 Null / AIR 제거
     */
    fun getItems(): List<ItemStack> = tradeItems.toList().filter { !isNullOrAir(it) }

    var isConfirmed = false
        private set

    fun confirm() {
        isConfirmed = true
    }

    fun openUI() {
        player.openFrame(uiFrame?.getFrame() ?: return)
    }

    fun closeUI() {
        player.closeInventory()
    }

    fun sendMessage(msg: String) {
        player.sendMessage(msg)
    }

    fun sortItems() {
        val list = getItems()

        repeat(9) { i ->
            tradeItems.setItem(i, if(i < list.size) list[i] else AIR)
        }
    }

    fun registerItem(originalItem: ItemStack, isShiftClick: Boolean) {
        val item = originalItem.clone()

        if(isShiftClick)
            tradeItems.addItem(item).let { originalItem.amount = if(it.isEmpty()) 0 else it[0]!!.amount }
        else if(tradeItems.addItem(item.apply {amount = 1}).isEmpty())
            originalItem.amount--
    }

    fun unregisterItem(x: Int, y: Int, originalItem: ItemStack, isShiftClick: Boolean) {
        val tradeItem = tradeItems.getItem(y * 3 + x)!!
        val item = originalItem.clone()

        if(isShiftClick) {
            Items.addItem(player, item)
            tradeItem.amount = 0
            sortItems()
        } else {
            Items.addItem(player, item.apply {amount = 1})
            if(--tradeItem.amount == 0) sortItems()

        }
    }
}

enum class TradeState {
    PROCEED,
    DENIED,
    SUCCESS,
    END
    ;
}