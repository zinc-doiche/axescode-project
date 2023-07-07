package com.github.axessystem.util.ui

import org.bukkit.entity.Player

/**
 *  [UI]를 제어하는 객체를 나타냅니다.
 */
interface Visualize<U: UI> {

    /**
     * @see [UI.getFrame]
     */
    var uiFrame: U?

    /**
     * @see [UI.getFrame]
     * @see [Player.openInventory]
     */
    fun openUI()

    /**
     * @see [UI.getFrame]
     * @see [Player.closeInventory]
     */
    fun closeUI()
}