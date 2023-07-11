package com.github.axessystem.listener;

import com.github.axessystem.util.ui.UITemplate
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class UIListener: Listener {
    @EventHandler
    fun onOpen(e: InventoryOpenEvent) {
        val ui = usingUITemplates[e.player] ?: return
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val ui = usingUITemplates[e.whoClicked] ?: return
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val ui = usingUITemplates[e.player] ?: return

        when(e.reason) {
            InventoryCloseEvent.Reason.PLUGIN -> {
                ui.onPluginClose?.invoke(e) ?: return
            }
            InventoryCloseEvent.Reason.OPEN_NEW -> {
                ui.onOpenNewClose?.invoke(e) ?: return
            }
            InventoryCloseEvent.Reason.PLAYER -> {
                ui.onPlayerClose?.invoke(e) ?: return
            }
            else -> {
                ui.onElseClose?.invoke(e) ?: return
            }
        }
    }

    companion object {
        val usingUITemplates: HashMap<Player, UITemplate> = HashMap()
    }
}
