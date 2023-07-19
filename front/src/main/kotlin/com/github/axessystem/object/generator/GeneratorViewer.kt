package com.github.axessystem.`object`.generator

import com.github.axescode.inventory.handler.Viewer
import org.bukkit.entity.Player

class GeneratorViewer(
    private val playerEntity: Player
): Viewer {
    var genUI: GeneratorUI? = null

    override fun getPlayer(): Player = playerEntity
    override fun getHandler(): GeneratorUI? = genUI
    override fun openUI() { genUI?.openUI() }
    override fun closeUI() { player.closeInventory() }
}