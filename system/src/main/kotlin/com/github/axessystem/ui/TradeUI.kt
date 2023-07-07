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
    private val info: ItemStack = getCustomItem(Material.PAPER, text("도움말").decoration(TextDecoration.BOLD, true), 10002) { meta ->
        meta.lore(texts("", "   - SHIFT + 클릭 : 세트 단위로 등록", "   - 클릭 : 1개 단위로 등록"))
    }

    private val cancel: ItemStack = getCustomItem(Material.PAPER, text("나가기").color(Colors.red).decoration(TextDecoration.BOLD, true), 10003) { meta ->
        meta.lore(texts("", "거래를 종료합니다."))
    }

    private val confirm: ItemStack = getCustomItem(Material.PAPER, text("아이템 확정").decoration(TextDecoration.BOLD, true), 10004) { meta ->
        meta.lore(texts("", "거래 품목을 확정합니다.", "이후 품목을 바꿀 수 없습니다."))
    }

    private val trade: ItemStack = getCustomItem(Material.PAPER, text("거래 완료").decoration(TextDecoration.BOLD, true), 10004) { meta ->
        meta.lore(texts("", "거래를 완료합니다."))
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
        addItem(tradeData.acceptor.player, *tradeData.acceptor.getItems)
        addItem(tradeData.requester.player, *tradeData.requester.getItems)
    }

    //저장 시 서브루틴
    private val lazySave: Deferred<*> = pluginScope.async(start = CoroutineStart.LAZY) {
        tradeData.sendMessageAll("거래 기록 저장 중...")
        tradeData.saveData()

        tradeData.sendMessageAll("거래 진행 중...")
        addItem(tradeData.requester.player, *tradeData.acceptor.getItems)
        addItem(tradeData.acceptor.player, *tradeData.requester.getItems)

        tradeData.sendMessageAll("거래 완료!")
    }

    //메인 프레임
    override fun getFrame(): InvFrame {
        return InvFX.frame(6, text("거래").decoration(TextDecoration.BOLD, true)) {
            onClickBottom { e ->
                if(viewer.isConfirmed || isNullOrAir(e.currentItem)) return@onClickBottom

                viewer.registerItem(e)
//                tradeData.openAll()
                viewer.openUI()
            }

            slot(0, 0) {item = info}
            slot(2, 0) {item = acceptorHead}
            slot(6, 0) {item = requesterHead}
            slot(8, 0) {
                item = cancel
                onClick { tradeData.deny() }
            }

            if(!viewer.isConfirmed && !viewer.tradeItems.isEmpty) {
                slot(if(tradeData.acceptor === viewer) 2 else 6, 4) {
                    item = confirm
                    onClick { viewer.confirm() }
                }
            }

            if(tradeData.acceptor.isConfirmed && tradeData.requester.isConfirmed) {
                slot(4, 4) {
                    item = trade
                    onClick { tradeData.success() }
                }
            }

            list(1, 1, 3, 3, true, {tradeData.acceptor.tradeItems.toList()}) {
                transform {it}
                if(tradeData.acceptor === viewer && !viewer.isConfirmed) onClickItem { x, y, _, event ->
                    if(isNullOrAir(event.currentItem)) return@onClickItem
                    viewer.unregisterItem(x, y, event)
//                tradeData.openAll()
                    viewer.openUI()
                }
            }

            list(5, 1, 7, 3, true, {tradeData.requester.tradeItems.toList()}) {
                transform {it}
                if(tradeData.acceptor !== viewer && !viewer.isConfirmed) onClickItem { x, y, _, event ->
                    if(isNullOrAir(event.currentItem)) return@onClickItem
                    viewer.unregisterItem(x, y, event)
//                tradeData.openAll()
                    viewer.openUI()
                }
            }

            onClose { e ->
                info(e.reason.name)
                when(tradeData.tradeState) {
                    TradeState.PROCESSING -> {
                        when(e.reason) {
                            InventoryCloseEvent.Reason.PLAYER -> {
                                viewer.sendMessage("거래가 중지되었습니다.")
                                if(tradeData.acceptor !== viewer) {
                                    tradeData.acceptor.sendMessage("상대가 거래를 종료하였습니다.")
                                    tradeData.acceptor.closeUI()
                                }
                                else {
                                    tradeData.requester.sendMessage("상대가 거래를 종료하였습니다.")
                                    tradeData.requester.closeUI()
                                }
                            }
                            InventoryCloseEvent.Reason.OPEN_NEW -> return@onClose
                            else -> {
                                tradeData.sendMessageAll("오류가 발생하여 거래가 중지되었습니다.")
                                if(tradeData.acceptor !== viewer) tradeData.acceptor.closeUI()
                                else tradeData.requester.closeUI()
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

