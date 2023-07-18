package com.github.axessystem.`object`.ui

import com.github.axescode.core.ui.UIHandler
import com.github.axescode.core.ui.UITemplates
import com.github.axescode.core.ui.template.UI
import org.bukkit.event.inventory.InventoryType

class InputUI: UIHandler {
    override fun openUI() {
        TODO("Not yet implemented")
    }

    private val ui: UI = UITemplates.createUI(InventoryType.ANVIL) { ui ->

    }
}
