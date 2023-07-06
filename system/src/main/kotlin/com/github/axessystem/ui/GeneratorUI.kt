package com.github.axessystem.ui

import com.github.axescode.util.Colors
import com.github.axescode.util.Items.item
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.util.text
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

object GeneratorUI {
    private val viewerPages = HashMap<Player, Int>()
    private val leftBtn = item(Material.PAPER) { it.displayName(text("<")) }
    private val rightBtn = item(Material.PAPER) { it.displayName(text(">")) }

    fun get(player: Player): InvFrame {
        if(!viewerPages.containsKey(player)) viewerPages[player] = 0
        var idx = viewerPages[player] ?: 0

        val pagination = Pagination(BlockGeneratorData.getAllData.filter { data -> data.location.world == player.world }
            .sortedBy { data -> data.generator.generatorName }, 45)

        return InvFX.frame(6, text("생성기 관리메뉴").decoration(TextDecoration.BOLD, true)) {
            list(0, 1, 8, 5, true, {
                pagination.getPagedList(idx)
            }) {
                transform { data ->
                    data.generator.item.apply {
                        editMeta { meta ->
                            meta.displayName(meta.displayName()?.append(text(" [${data.location.toVector()}]")))
                            meta.lore(meta.lore()?.apply {
                                add(text(""))
                                add(text("클릭하여 순간이동").color(Colors.beige).decoration(TextDecoration.BOLD, true))
                            })
                        }
                    }
                }
                onClickItem { _, _, item, _ ->
                    player.teleportAsync(item.first.location)
                }
            }
            if(idx != 0) {
                slot(0, 0) {
                    item = leftBtn

                    onClick {
                        viewerPages[player] = --idx
                        player.openFrame(get(player))
                    }
                }
            }
            if(idx != pagination.totalPage - 1) {
                slot(8, 0) {
                    item = rightBtn

                    onClick {
                        viewerPages[player] = ++idx
                        player.openFrame(get(player))
                    }
                }
            }

            slot(4, 0) {
                item = item(Material.PAPER) {
                    it.displayName(text("${idx + 1} / ${pagination.totalPage}"))
                }

                onClick {
                    viewerPages[player] = --idx
                    player.openFrame(get(player))
                }
            }

            onClose {
                if(it.reason != InventoryCloseEvent.Reason.PLUGIN) {
                    viewerPages.remove(player)
                }
            }
        }
    }
}