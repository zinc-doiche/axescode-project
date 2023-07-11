package com.github.axessystem.util

import org.bukkit.Location
import org.bukkit.NamespacedKey

interface Placeable {
    fun onPlace(location: Location)

    companion object {
        const val AXESCODE = "axescode"
        const val PLACEABLE_KEY = "placeable"
        val placeableKey: NamespacedKey = NamespacedKey(AXESCODE, PLACEABLE_KEY)
    }
}

