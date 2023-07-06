package com.github.axessystem.`object`.trade

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime

data class TradeData(
    val tradeId: Long? = null,
    val acceptor: Trader,
    val requester: Trader
)

data class Trader(
    val player: Player,
    val tradeMoney: Long = 0
) {
    val clickedSlots = HashSet<Int>()
    val tradeItems: Inventory = Bukkit.createInventory(null, InventoryType.DISPENSER)

    fun view() {
        player.openInventory(tradeItems)
    }

}