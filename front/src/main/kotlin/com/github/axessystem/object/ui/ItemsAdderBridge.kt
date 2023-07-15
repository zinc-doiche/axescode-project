package com.github.axessystem.`object`.ui

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import org.bukkit.entity.Player

object ItemsAdderBridge {
    fun Player.setUI(title: String, guiName: String) {
        TexturedInventoryWrapper.setPlayerInventoryTexture(this@setUI, FontImageWrapper(guiName), title, 0, -8)
    }
}