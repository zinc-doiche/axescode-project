package com.github.axessystem.`object`.trade

import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.core.trade.TradeVO
import com.github.axescode.util.Items
import com.github.axescode.util.Items.AIR
import com.github.axescode.util.Items.isNullOrAir
import com.github.axessystem.ui.TradeUI
import com.github.axessystem.util.encodedItem
import com.github.axessystem.util.useOutputStream
import com.github.axessystem.util.writeItem
import com.github.mckd.ui.UITemplates
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * @see [TradeData.startTrade]
 */
data class TradeData(
    val acceptor: Trader,
    val requester: Trader
) {
    var tradeState = TradeState.PROCEED
        private set

    val isAllReady: Boolean
        get()= acceptor.decision == Decision.READY && requester.decision == Decision.READY

    val isAllConfirmed: Boolean
        get() = acceptor.decision == Decision.CONFIRM && requester.decision == Decision.CONFIRM

    /**
     * 거래를 시작합니다.
     */
    fun startTrade() {
        acceptor.tradeUI = TradeUI(this@TradeData, acceptor)
        requester.tradeUI = TradeUI(this@TradeData, requester)

        UITemplates.addViewer(acceptor.player)
        UITemplates.addViewer(requester.player)
        openAll()
    }

    /**
     * 거래상태를 [TradeState.DENIED]로 바꾸고 거래를 종료합니다.
     */
    fun denyTrade() {
        tradeState = TradeState.DENIED
        UITemplates.removeViewer(acceptor.player)
        UITemplates.removeViewer(requester.player)
    }

    /**
     * 거래상태를 [TradeState.SUCCESS]로 바꾸고 거래를 종료합니다.
     */

    fun successTrade() {
        tradeState = TradeState.SUCCESS
        UITemplates.removeViewer(acceptor.player)
        UITemplates.removeViewer(requester.player)
    }

    fun openAll() {
        acceptor.openUI()
        //requester.openUI()
    }

    fun closeAll() {
        acceptor.closeUI()
        //requester.closeUI()
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

/**
 * 거래의 진행 상태를 나타냅니다.
 * @see [TradeData]
 */
enum class TradeState {
    /**
     * 거래 진행 중인 상태입니다.
     */
    PROCEED,
    /**
     * 거래가 거절된 상태입니다.
     */
    DENIED,
    /**
     * 거래가 성공한 상태입니다.
     */
    SUCCESS,
    ;
}

data class Trader(
    val playerData: PlayerData,
    var tradeMoney: Long = 0
) {
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)
    var tradeUI: TradeUI? = null
    var decision: Decision = Decision.NOT_READY

    val player: Player
        get() = playerData.playerEntity

    /**
     * [Inventory] 컬렉션화에서의 Null / AIR 제거
     */
    fun getItems(): List<ItemStack> = tradeItems.toList().filter { !isNullOrAir(it) }
    fun openUI() { tradeUI?.openUI() }
    fun closeUI() { player.closeInventory() }
    fun sendMessage(msg: String) { player.sendMessage(msg) }

    /**
     * 인벤토리 내부 아이템을 빈칸 없도록 정렬
     */
    private fun sortItems() {
        val list = getItems()
        repeat(9) { i ->
            tradeItems.setItem(i, if(i < list.size) list[i] else AIR)
        }
    }

    /**
     * 아이템 등록 후 가상 거래 인벤토리 정렬
     *  @see [Trader.sortItems]
     */
    fun registerItem(originalItem: ItemStack, isShiftClick: Boolean) {
        val item = originalItem.clone()

        if(isShiftClick)
            tradeItems.addItem(item).let { originalItem.amount = if(it.isEmpty()) 0 else it[0]!!.amount }
        else if(tradeItems.addItem(item.apply {amount = 1}).isEmpty())
            originalItem.amount--
    }

    /**
     * 아이템 등록 해제 후 가상 거래 인벤토리 정렬
     * @see [Trader.sortItems]
     */
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

    override fun equals(other: Any?): Boolean {
        if(other !is Trader) return false
        return playerData.playerId == other.playerData.playerId
    }
}

/**
 * 거래 아이템과 재화에 관한 [Trader]의 의사 상태를 나타냅니다.
 */
enum class Decision {
    /**
     * 거래 아이템과 재화가 준비되지 않은 상태입니다.
     */
    NOT_READY,
    /**
     * 거래 아이템과 재화가 모두 준비된 상태입니다.
     */
    READY,
    /**
     * 거래를 확정지은 상태입니다.
     */
    CONFIRM
    ;
}