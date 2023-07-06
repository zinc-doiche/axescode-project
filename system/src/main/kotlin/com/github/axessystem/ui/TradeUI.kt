package com.github.axessystem.ui

import com.github.axescode.util.Colors
import com.github.axescode.util.Items
import com.github.axescode.util.Items.isNullOrAir
import com.github.axescode.util.Items.item
import com.github.axessystem.info
import com.github.axessystem.`object`.trade.TradeData
import com.github.axessystem.`object`.trade.TradeState
import com.github.axessystem.`object`.trade.Trader
import com.github.axessystem.pluginScope
import com.github.axessystem.util.text
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.collections.HashMap

object TradeUI {
    fun getFrame(tradeData: TradeData, viewer: Trader): InvFrame {
        val acceptorHead = item(Material.PLAYER_HEAD) { meta ->
            meta as SkullMeta
            meta.owningPlayer = tradeData.acceptor.playerData.playerEntity
            meta.displayName(text(tradeData.acceptor.playerData.playerEntity.name).color(Colors.gold))
        }

        val requesterHead = item(Material.PLAYER_HEAD) { meta ->
            meta as SkullMeta
            meta.owningPlayer = tradeData.requester.playerData.playerEntity
            meta.displayName(text(tradeData.requester.playerData.playerEntity.name).color(Colors.gold))
        }

        //거레 취소 시 아이템 돌려주는 롤백용 서브루틴
        val lazyRollback = pluginScope.launch(start = CoroutineStart.LAZY) {
            "거래가 취소되었습니다. 아이템을 회수합니다.".let {
                tradeData.acceptor.playerData.playerEntity.sendMessage(it)
                tradeData.requester.playerData.playerEntity.sendMessage(it)
            }

            if(!tradeData.acceptor.tradeItems.isEmpty)
                Items.addItem(
                    tradeData.acceptor.playerData.playerEntity,
                    *tradeData.acceptor.tradeItems.toList().toTypedArray()
                )

            if(!tradeData.requester.tradeItems.isEmpty)
                Items.addItem(
                    tradeData.requester.playerData.playerEntity,
                    *tradeData.requester.tradeItems.toList().toTypedArray()
                )
        }

        //저장 시 서브루틴
        val lazySave = pluginScope.async(start = CoroutineStart.LAZY) {
            "거래 기록 저장 중...".let {
                tradeData.acceptor.playerData.playerEntity.sendMessage(it)
                tradeData.requester.playerData.playerEntity.sendMessage(it)
            }

            tradeData.saveData()

            "거래 진행 중...".let {
                tradeData.acceptor.playerData.playerEntity.sendMessage(it)
                tradeData.requester.playerData.playerEntity.sendMessage(it)
            }

            if(!tradeData.acceptor.tradeItems.isEmpty)
                Items.addItem(
                    tradeData.requester.playerData.playerEntity,
                    *tradeData.acceptor.tradeItems.toList().toTypedArray()
                )

            if(!tradeData.requester.tradeItems.isEmpty)
                Items.addItem(
                    tradeData.acceptor.playerData.playerEntity,
                    *tradeData.requester.tradeItems.toList().toTypedArray()
                )

            "거래 완료!.".let {
                tradeData.acceptor.playerData.playerEntity.sendMessage(it)
                tradeData.requester.playerData.playerEntity.sendMessage(it)
            }
        }

        //메인 프레임
        return InvFX.frame(6, text("거래").decoration(TextDecoration.BOLD, true)) {
            slot(2, 0) {item = acceptorHead}
            slot(6, 0) {item = requesterHead}

            list(1, 1, 3, 3, true, {tradeData.acceptor.tradeItems.toList()}) {
                transform {it}
            }
            list(5, 1, 7, 3, true, {tradeData.requester.tradeItems.toList()}) {
                transform {it}
            }

            onClickBottom { e ->
                if(isNullOrAir(e.currentItem) || viewer.isFull) return@onClickBottom
                val item = e.currentItem!!.clone()
                val exceeds: HashMap<Int, ItemStack>

                if(e.click.isShiftClick) {
                    exceeds = viewer.tradeItems.addItem(item)
                    if(exceeds.isNotEmpty()) {
                        viewer.isFull = true
                        e.currentItem!!.amount = exceeds[0]!!.amount
                    } else
                        e.currentItem!!.amount = 0
                } else {
                    exceeds = viewer.tradeItems.addItem(item.apply {amount = 1})

                    if(exceeds.isNotEmpty())
                        viewer.isFull = true
                    else
                        e.currentItem!!.amount--
                }

                viewer.tradeItems.forEach { viewer.playerData.playerEntity.sendMessage(it.toString()) }
            }

            onClose { e ->
                info(e.reason.name)
                when(tradeData.tradeState) {
                    TradeState.PROCESSING -> {
                        if(e.reason == InventoryCloseEvent.Reason.PLAYER)
                            lazyRollback.start()
                    }
                    TradeState.DENIED -> {
                        lazyRollback.start()
                    }
                    TradeState.SUCCESS -> {
                        lazySave.start()
                    }
                }
            }
        }
    }
}

