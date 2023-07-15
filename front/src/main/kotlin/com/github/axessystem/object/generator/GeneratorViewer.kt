package com.github.axessystem.`object`.generator

import com.github.axescode.core.ui.Viewer
import org.bukkit.entity.Player

class GeneratorViewer(
    val player: Player
): Viewer {
    var genUI: GeneratorUI? = null

    override fun getHandler(): GeneratorUI? = genUI
    override fun openUI() { genUI?.openUI() }
    override fun closeUI() { player.closeInventory() }
}