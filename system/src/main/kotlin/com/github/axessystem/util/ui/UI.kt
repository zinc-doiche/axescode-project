package com.github.axessystem.util.ui

import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.InvFX

/**
 * [InvFX]를 통한 UI 객체를 나타냅니다.
 */
interface UI {
    /**
     * UI를 가져옵니다.
     */
    fun getFrame(): InvFrame
}