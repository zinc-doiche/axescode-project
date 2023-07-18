package com.github.axessystem.`object`.shop

import com.github.axescode.core.ui.UIHandler
import com.github.axescode.core.ui.UITemplates
import com.github.axescode.core.ui.template.UI
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