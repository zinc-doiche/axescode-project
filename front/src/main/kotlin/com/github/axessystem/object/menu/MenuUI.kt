package com.github.axessystem.`object`.menu

import com.github.axescode.inventory.UITemplates
import com.github.axescode.inventory.ui.UI
import com.github.axessystem.util.text
import org.bukkit.entity.Player

object MenuUI {
    private val ui: UI = UITemplates.createSquareUI(6, text("메뉴")) {

    }

    fun openUI(player: Player) {
        ui.openUI(player)
    }
}