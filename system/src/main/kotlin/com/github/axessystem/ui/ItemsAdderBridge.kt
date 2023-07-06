package com.github.axessystem.ui

import com.github.axessystem.pluginScope
import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.bukkit.entity.Player

object ItemsAdderBridge {
    fun Player.setUI(title: String, guiName: String) {
        pluginScope.async {
            delay(1000L)
            TexturedInventoryWrapper.setPlayerInventoryTexture(this@setUI, FontImageWrapper(guiName), title, 0, -8)
        }
    }
}