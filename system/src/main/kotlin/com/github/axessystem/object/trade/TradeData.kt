package com.github.axessystem.`object`.trade

import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.core.trade.TradeVO
import com.github.axescode.util.Items
import com.github.axescode.util.Items.isNullOrAir
import com.github.axessystem.info
import com.github.axessystem.pluginScope
import com.github.axessystem.ui.TradeUI
import com.github.axessystem.util.ui.Visualize
import com.github.axessystem.util.useOutputStream
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.async
import org.bukkit.Bukkit
import org.bukkit.block.Dispenser
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

data class TradeData(
    val acceptor: Trader,
    val requester: Trader
) {
    var tradeState = TradeState.PROCESSING
        private set

    fun startTrade() {
        pluginScope.async {
            acceptor.uiFrame = TradeUI(this@TradeData, acceptor)
            requester.uiFrame = TradeUI(this@TradeData, requester)

//            openAll()
            requester.openUI()
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
}

data class Trader(
    val playerData: PlayerData,
    var tradeMoney: Long = 0
): Visualize<TradeUI> {
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)
    override var uiFrame: TradeUI? = null

    val player: Player
        get() = playerData.playerEntity

    /**
     * Inventory 컬렉션화에서의 Null 제거
     */
    val getItems: Array<ItemStack>
        get() = tradeItems.toList().filterNotNull().toTypedArray()

    var isConfirmed = false
        private set

    fun confirm() {
        isConfirmed = true
        openUI()
    }

    override fun openUI() {
        player.openFrame(uiFrame?.getFrame() ?: return)
    }

    override fun closeUI() {
        player.closeInventory()
    }

    fun sendMessage(msg: String) {
        player.sendMessage(msg)
    }

    fun registerItem(event: InventoryClickEvent) {
        val item = event.currentItem!!.clone()
        val exceeds: HashMap<Int, ItemStack>

        if(event.click.isShiftClick) {
            exceeds = tradeItems.addItem(item)

            event.currentItem!!.amount = if(exceeds.isNotEmpty()) exceeds[0]!!.amount else 0
        } else {
            exceeds = tradeItems.addItem(item.apply {amount = 1})

            if(exceeds.isEmpty())
                event.currentItem!!.amount--
        }
    }

    fun unregisterItem(x: Int, y: Int, event: InventoryClickEvent) {
        val slot = y * 3 + x
        val item = event.currentItem!!
        info(slot)
        tradeItems.getItem(slot)?.let { info(it) }

        if(event.click.isShiftClick) {
            Items.addItem(player, item)
            tradeItems.getItem(slot)!!.amount = 0
        } else {
            Items.addItem(player, item.apply {amount = 1})
            tradeItems.getItem(slot)!!.amount--
        }
    }
}

enum class TradeState {
    PROCESSING,
    DENIED,
    SUCCESS
    ;
}