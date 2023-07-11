package com.github.axessystem.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

internal fun useOutputStream(use: (ByteArrayOutputStream, BukkitObjectOutputStream) -> Unit) {
    ByteArrayOutputStream().use { bs ->
        BukkitObjectOutputStream(bs).use { os ->
            use(bs, os)
        }
    }
}

internal fun useInputStream(byteArray: ByteArray, use: (ByteArrayInputStream, BukkitObjectInputStream) -> Unit) {
    ByteArrayInputStream(byteArray).use { bs ->
        BukkitObjectInputStream(bs).use { input ->
            use(bs, input)
        }
    }
}

internal fun BukkitObjectOutputStream.writeItem(item: ItemStack) {
    item.serialize().let(this::writeObject)
    flush()
}

internal val ByteArrayOutputStream.encodedItem: String
    get() = Base64.getEncoder().encodeToString(toByteArray())

internal fun Base64.Decoder.decodeAsItem(src: String): ItemStack? {
    var item: ItemStack? = null
    useInputStream(decode(src)) { bs, input ->
        item = ItemStack.deserialize(input.readObject() as MutableMap<String, Any>)
    }
    return item
}