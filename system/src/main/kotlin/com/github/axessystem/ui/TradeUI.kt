package com.github.axessystem.ui

import com.github.axessystem.util.text
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import net.kyori.adventure.text.format.TextDecoration

object TradeUI {
    fun getFrame(): InvFrame {
        return InvFX.frame(6, text("거래").decoration(TextDecoration.BOLD, true)) {

        }
    }
}