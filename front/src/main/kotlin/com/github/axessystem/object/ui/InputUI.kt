package com.github.axessystem.`object`.ui

import com.github.axescode.inventory.handler.UIHandler
import com.github.axescode.inventory.UITemplates
import com.github.axescode.inventory.ui.UI
import org.bukkit.event.inventory.InventoryType

class InputUI: UIHandler {
    override fun openUI() {
        TODO("Not yet implemented")
    }

    private val ui: UI = UITemplates.createUI(InventoryType.ANVIL) { ui ->

    }
}
