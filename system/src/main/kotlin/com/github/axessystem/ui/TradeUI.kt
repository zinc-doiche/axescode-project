package com.github.axessystem.ui

import com.github.axescode.util.Colors
import com.github.axescode.util.Items
import com.github.axescode.util.Items.*
import com.github.axessystem.info
import com.github.axessystem.`object`.trade.TradeData
import com.github.axessystem.`object`.trade.TradeState
import com.github.axessystem.`object`.trade.Trader
import com.github.axessystem.pluginScope
import com.github.axessystem.util.text
import com.github.axessystem.util.texts
import com.github.axessystem.util.ui.UI
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.*
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.collections.HashMap

class TradeUI(
    private val tradeData: TradeData,
    private val viewer: Trader
): UI {
    private val info: ItemStack = getCustomItem(Material.PAPER, text("도움말"), 10002) { meta ->
        meta.lore(texts("", "SHIFT+클릭: 세트 단위로 등록", "클릭: 1개 단위로 등록"))
    }

    private val cancel: ItemStack = getCustomItem(Material.PAPER, text("나가기").color(Colors.red), 10003) { meta ->
        meta.lore(texts("거래를 종료합니다."))
    }

    private val acceptorHead: ItemStack = item(Material.PLAYER_HEAD) { meta ->
        meta as SkullMeta
        meta.owningPlayer = tradeData.acceptor.player
        meta.displayName(text(tradeData.acceptor.player.name).color(Colors.gold))
    }

    private val requesterHead: ItemStack = item(Material.PLAYER_HEAD) { meta ->
        meta as SkullMeta
        meta.owningPlayer = tradeData.requester.player
        meta.displayName(text(tradeData.requester.player.name).color(Colors.gold))
    }

    //거레 취소 시 아이템 돌려주는 롤백용 서브루틴
    private val lazyRollback: Job = pluginScope.launch(start = CoroutineStart.LAZY) {
        tradeData.sendMessageAll("거래가 취소되었습니다. 아이템을 회수합니다.")

        if(!tradeData.acceptor.tradeItems.isEmpty)
            addItem(
                tradeData.acceptor.player,
                *tradeData.acceptor.tradeItems.toList().toTypedArray()
            )

        if(!tradeData.requester.tradeItems.isEmpty)
            addItem(
                tradeData.requester.player,
                *tradeData.requester.tradeItems.toList().toTypedArray()
            )
    }

    //저장 시 서브루틴
    private val lazySave: Deferred<*> = pluginScope.async(start = CoroutineStart.LAZY) {
        tradeData.sendMessageAll("거래 기록 저장 중...")
        tradeData.saveData()

        tradeData.sendMessageAll("거래 진행 중...")
        if(!tradeData.acceptor.tradeItems.isEmpty)
            addItem(
                tradeData.requester.player,
                *tradeData.acceptor.tradeItems.toList().toTypedArray()
            )

        if(!tradeData.requester.tradeItems.isEmpty)
            addItem(
                tradeData.acceptor.player,
                *tradeData.requester.tradeItems.toList().toTypedArray()
            )

        tradeData.sendMessageAll("거래 완료!")
    }

    //메인 프레임
    override fun getFrame(): InvFrame {
        return InvFX.frame(6, text("거래").decoration(TextDecoration.BOLD, true)) {
            slot(0, 0) {item = info}
            slot(2, 0) {item = acceptorHead}
            slot(6, 0) {item = requesterHead}
            slot(8, 0) {
                item = cancel
                onClick { tradeData.deny() }
            }

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

                tradeData.openAll()
                viewer.tradeItems.forEach { viewer.sendMessage(it.toString()) }
            }

            onClose { e ->
                info(e.reason.name)
                when(tradeData.tradeState) {
                    TradeState.PROCESSING -> {
                        when(e.reason) {
                            InventoryCloseEvent.Reason.PLAYER -> {
                                viewer.sendMessage("거래 도즁에 창을 닫을 수 없습니다!")
                                viewer.openUI()
                            }
                            InventoryCloseEvent.Reason.OPEN_NEW -> return@onClose
                            else -> {
                                tradeData.sendMessageAll("오류가 발생하여 거래가 중지되었습니다.")
                                tradeData.closeAll()
                            }
                        }
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

