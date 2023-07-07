package com.github.axessystem.util

import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream

internal fun useOutputStream(consumer: (ByteArrayOutputStream, BukkitObjectOutputStream) -> Unit) {
    ByteArrayOutputStream().use {
        BukkitObjectOutputStream(it).use { os ->
            consumer(it, os)
        }
    }
}