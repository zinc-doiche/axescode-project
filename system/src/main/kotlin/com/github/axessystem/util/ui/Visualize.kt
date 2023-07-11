package com.github.axessystem.util.ui

import org.bukkit.entity.Player

/**
 *  [UITemplate]를 제어하는 객체를 나타냅니다.
 */
interface Visualize<U: UITemplate> {

    /**
     * @see [UITemplate.getFrame]
     */
    var uiFrame: U?

    /**
     * @see [UITemplate.getFrame]
     * @see [Player.openInventory]
     */
    fun openUI()

    /**
     * @see [UITemplate.getFrame]
     * @see [Player.closeInventory]
     */
    fun closeUI()
}