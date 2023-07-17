package com.github.axessystem.`object`.menu

import com.github.axescode.core.ui.UIHandler
import com.github.axescode.core.ui.UITemplates
import com.github.axescode.core.ui.template.UITemplate
import com.github.axessystem.util.text
import org.bukkit.entity.Player

object MenuUI {
    private val ui: UITemplate = UITemplates.createUI(6, text("메뉴")) {

    }

    fun openUI(player: Player) {
        ui.openUI(player)
    }
}