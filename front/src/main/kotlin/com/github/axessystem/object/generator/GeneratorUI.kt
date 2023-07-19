package com.github.axessystem.`object`.generator

import com.github.axescode.inventory.handler.UIHandler
import com.github.axescode.inventory.UITemplates
import com.github.axescode.inventory.ui.UI
import com.github.axescode.util.Colors
import com.github.axescode.util.Items.getCustomItem
import com.github.axescode.util.Items.item
import com.github.axessystem.info
import com.github.axessystem.`object`.ui.Pagination
import com.github.axessystem.util.text
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class GeneratorUI(
    private val viewer: GeneratorViewer
): UIHandler {
    companion object {
        private val leftBtn = getCustomItem(Material.PAPER, text("이전"), 10005) {}
        private val rightBtn = getCustomItem(Material.PAPER, text("다음"), 10006) {}
    }

    private var idx = 0
    private val pagination: Pagination<BlockGeneratorData>
        get() = Pagination(BlockGeneratorData.getAllData
                        .filter { it.location.world == viewer.player.world }
                        .sortedBy { it.generator.generatorName }, 45)

    override fun openUI() { ui.openUI(viewer.player) }

    private val ui: UI = UITemplates.createSquareUI(6, text("생성기 관리메뉴").decoration(TextDecoration.BOLD, true)) { ui ->
        ui.setOnOpen {
            info("open")
            repeat(45) { i ->
                val x = i % 9
                val y = i / 9

                if(pagination.getPagedList(idx).size <= i) return@repeat
                val data = pagination.getPagedList(idx)[i]

                ui.setSlot(x, y + 1) { slot ->
                    slot.item = data.generator.item.apply {
                        editMeta { meta ->
                            meta.displayName( meta.displayName()?.append(text(" [${data.location.toVector()}]")))
                            meta.lore(meta.lore()?.apply {
                                add(text(""))
                                add(text("클릭하여 순간이동").color(Colors.beige).decoration(TextDecoration.BOLD, true))
                            })
                        }
                    }
                    slot.setOnClick { viewer.player.teleportAsync(data.location) }
                }
            }

            ui.setSlot(4, 0) { it.item = item(Material.PAPER, text("${idx + 1} / ${pagination.totalPage}")) }

            if (idx != 0) {
                ui.setSlot(0, 0) { slot ->
                    slot.item = leftBtn
                    slot.setOnClick {
                        idx--
                        //update
                        viewer.openUI()
                    }
                }
            }
            if (idx != pagination.totalPage - 1) {
                ui.setSlot(8, 0) { slot ->
                    slot.item = rightBtn
                    slot.setOnClick {
                        idx++
                        //update
                        viewer.openUI()
                    }
                }
            }
        }
    }
}