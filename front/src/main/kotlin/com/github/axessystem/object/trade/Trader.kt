package com.github.axessystem.`object`.trade

import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.ui.Viewer
import com.github.axescode.util.Items
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

data class Trader(
    val playerData: PlayerData,
    var tradeMoney: Long = 0
): Viewer {
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)
    var tradeUI: TradeUI? = null
    var decision: Decision = Decision.NOT_READY

    val player: Player = playerData.playerEntity

    /**
     * [Inventory] 컬렉션화에서의 Null / AIR 제거
     */
    fun getItems(): List<ItemStack> = tradeItems.toList().filter { !Items.isNullOrAir(it) }
    override fun openUI() { tradeUI?.openUI() }
    override fun closeUI() { player.closeInventory() }
    override fun getHandler(): TradeUI? = tradeUI

    fun sendMessage(msg: String) { player.sendMessage(msg) }

    /**
     * 인벤토리 내부 아이템을 빈칸 없도록 정렬
     */
    private fun sortItems() {
        val list = getItems()
        repeat(9) { i ->
            tradeItems.setItem(i, if(i < list.size) list[i] else Items.AIR)
        }
    }

    /**
     * 아이템 등록 후 가상 거래 인벤토리 정렬
     *  @see Trader.sortItems
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
     * @see Trader.sortItems
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

    override fun hashCode(): Int {
        return playerData.hashCode()
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