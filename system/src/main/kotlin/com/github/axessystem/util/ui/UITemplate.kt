package com.github.axessystem.util.ui

import com.github.axescode.container.Container
import com.github.axescode.container.Data
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.jvm.optionals.getOrNull
import kotlin.properties.Delegates

/**
 * 화면을 구성하는 객체를 나타냅니다.
 */
interface UITemplate: Data {
    val lines: Int
    val inventory: Inventory

    fun getSlot(x: Int, y: Int): Slot?
    fun setSlot(x: Int, y: Int, init: () -> Slot)

    val onOpen: ((InventoryOpenEvent) -> Unit)?
    val onPluginClose: ((InventoryCloseEvent) -> Unit)?
    val onOpenNewClose: ((InventoryCloseEvent) -> Unit)?
    val onPlayerClose: ((InventoryCloseEvent) -> Unit)?
    val onElseClose: ((InventoryCloseEvent) -> Unit)?

    fun update() {;}
}

interface Slot {
    val coordinate: Pair<Int, Int>
    var item: ItemStack
    val isFixed: Boolean
}

/**
 * [UITemplate]를 보관합니다.
 */
object UITemplateContainer {
    private val container: Container<UITemplate> = object : Container<UITemplate>() {}
    operator fun get(key: String): UITemplate? = container.getData(key).getOrNull()

    fun register(key: String, inventory: Inventory, init: UIBuilder.() -> Unit): UITemplate {
        return UIBuilder().apply {
            this.inventory = inventory
            init(this)
        }.build().also { container.addData(key, it) }
    }

    fun register(key: String, init: UIBuilder.() -> Unit): UITemplate {
        return UIBuilder().apply(init).build().also { container.addData(key, it) }
    }

    fun has(key: String) = container.hasData(key)
    fun remove(key: String) = container.removeData(key)

    private data class UIImpl(
        override val lines: Int,
        override val onOpen: ((InventoryOpenEvent) -> Unit)?,
        override val onPluginClose: ((InventoryCloseEvent) -> Unit)?,
        override val onOpenNewClose: ((InventoryCloseEvent) -> Unit)?,
        override val onPlayerClose: ((InventoryCloseEvent) -> Unit)?,
        override val onElseClose: ((InventoryCloseEvent) -> Unit)?,
        override val inventory: Inventory
    ) : UITemplate, Cloneable {
        private val slots: HashMap<Pair<Int, Int>, Slot> = HashMap()

        override fun getSlot(x: Int, y: Int): Slot? = slots[x to y]

        override fun setSlot(x: Int, y: Int, init: () -> Slot) {
            slots[x to y] = init()
        }
    }

    class UIBuilder {
        var lines: Int by Delegates.notNull()
        var onOpen: ((InventoryOpenEvent) -> Unit)? = null
        var onPluginClose: ((InventoryCloseEvent) -> Unit)? = null
        var onOpenNewClose: ((InventoryCloseEvent) -> Unit)? = null
        var onPlayerClose: ((InventoryCloseEvent) -> Unit)? = null
        var onElseClose: ((InventoryCloseEvent) -> Unit)? = null
        var inventory: Inventory = Bukkit.createInventory(null, lines * 9)

        fun build(): UITemplate = UIImpl(lines, onOpen, onPluginClose, onOpenNewClose, onPlayerClose, onElseClose, inventory)
    }

    data class SlotImpl(
        override val coordinate: Pair<Int, Int>,
        override var item: ItemStack,
    ) : Slot {
        override val isFixed = false
    }

    data class ClickableSlot(
        override val coordinate: Pair<Int, Int>,
        override var item: ItemStack,
        val onClick: (InventoryClickEvent) -> Unit
    ): Slot {
        override val isFixed = true
    }
}