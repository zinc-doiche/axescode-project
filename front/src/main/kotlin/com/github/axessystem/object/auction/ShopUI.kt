package com.github.axessystem.`object`.auction

import com.github.axescode.inventory.handler.UIHandler
import com.github.axescode.inventory.UITemplates
import com.github.axescode.inventory.ui.UI
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ShopUI(
    val viewer: ShopViewer
): UIHandler {
    override fun openUI() {
        TODO("Not yet implemented")
    }

    private val ui: UI = UITemplates.createSquareUI(6, Component.text("상점")) { ui ->
        repeat(9) { i ->
            ui.setSlot(i, 0) { slot ->
                slot.item = ItemStack(Material.IRON_INGOT)
            }
        }
    }
}