package com.github.axessystem.`object`.trade

import com.github.axescode.core.ui.UIHandler
import com.github.axescode.util.Colors
import com.github.axescode.util.Items.*
import com.github.axescode.util.Sounds
import com.github.axessystem.pluginScope
import com.github.axessystem.util.text
import com.github.axessystem.util.texts
import com.github.axescode.core.ui.template.UI
import com.github.axescode.core.ui.UITemplates
import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import kotlinx.coroutines.*
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class TradeUI(
    private val tradeData: TradeData,
    private val viewer: Trader
): UIHandler {
    companion object {
        private val infoIcon: ItemStack = getCustomItem(Material.PAPER, text("도움말").decoration(TextDecoration.BOLD, true), 10002) { meta ->
            meta.lore(texts("", "   - SHIFT + 클릭 : 세트 단위로 등록 / 회수", "   - 일반 클릭 : 1개 단위로 등록 / 회수"))
        }

        private val cancelIcon: ItemStack = getCustomItem(Material.PAPER, text("나가기").color(Colors.red).decoration(TextDecoration.BOLD, true), 10003) { meta ->
            meta.lore(texts("", "거래를 종료합니다."))
        }

        private val confirmIcon: ItemStack = getCustomItem(Material.PAPER, text("아이템 확정").decoration(TextDecoration.BOLD, true), 10004) { meta ->
            meta.lore(texts(text(""), text("거래 품목을 확정합니다."), text("※이후 품목을 바꿀 수 없습니다.").color(Colors.red).decoration(TextDecoration.BOLD, true)))
        }

        private val tradeIcon: ItemStack = getCustomItem(Material.PAPER, text("거래 완료").decoration(TextDecoration.BOLD, true), 10004) { meta ->
            meta.lore(texts("", "거래를 완료합니다."))
        }

        private val notReady: ItemStack = item(Material.PAPER) { it.displayName(text("생각 중...")) }
        private val ready: ItemStack = item(Material.PAPER) { it.displayName(text("준비됨")) }
        private val confirm: ItemStack = item(Material.PAPER) { it.displayName(text("거래 수락!")) }

        private fun getDecisionIcon(decision: Decision): ItemStack = when(decision) {
            Decision.NOT_READY -> notReady
            Decision.READY -> ready
            Decision.CONFIRM -> confirm
        }
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

    // 거래 종료 전까지 UI 띄우는 루틴
    private fun lazyReOpen() {
        pluginScope.async {
            delay(50L)
            viewer.sendMessage("거래가 끝나기 전까지 창을 닫을 수 없습니다. 거래를 종료하시려면 X 버튼을 클릭해주세요.")
            openUI()
        }
    }

    override fun openUI() {
        ui.openUI(viewer.player)
        TexturedInventoryWrapper.setPlayerInventoryTexture(viewer.player, FontImageWrapper("axescode:trade"), "", 16, -9)
    }

    private val ui: UI = UITemplates.createSquareUI(5) { ui ->
        ui.setSlot(3, 4) { it.item = infoIcon }
        ui.setSlot(2, 0) { it.item = acceptorHead }
        ui.setSlot(6, 0) { it.item = requesterHead }

        ui.setSlot(if(viewer == tradeData.acceptor) 0 else 8, 3) {
            it.item = cancelIcon
            it.setOnClick {
                viewer.player.playSound(Sounds.click)
                tradeData.denyTrade()
                //end
                tradeData.closeAll()
            }
        }

        //아이템 등록
        ui.setOnClickBottom { e ->
            if (viewer.decision != Decision.NOT_READY || isNullOrAir(e.currentItem)) return@setOnClickBottom
            viewer.player.playSound(Sounds.click)
            viewer.registerItem(e.currentItem!!, e.isShiftClick)

            //update
            tradeData.openAll()
        }

        ui.setOnPlayerClose { if (tradeData.tradeState == TradeState.PROCEED) lazyReOpen() }
        ui.setOnElseClose {
            viewer.sendMessage("거래가 중지되었습니다.")
            if (tradeData.acceptor != viewer) tradeData.acceptor.sendMessage("상대가 거래를 종료하였습니다.")
            else tradeData.requester.sendMessage("상대가 거래를 종료하였습니다.")
            tradeData.lazyRollback()
        }
        ui.setOnPluginClose {
            when (tradeData.tradeState) {
                TradeState.PROCEED -> lazyReOpen()
                TradeState.DENIED -> {
                    viewer.sendMessage("거래가 중지되었습니다.")
                    if (tradeData.acceptor != viewer) tradeData.acceptor.sendMessage("상대가 거래를 종료하였습니다.")
                    else tradeData.requester.sendMessage("상대가 거래를 종료하였습니다.")
                    tradeData.lazyRollback()
                }

                TradeState.SUCCESS -> tradeData.lazySave()
            }
        }

        //열 때마다 갱신
        ui.setOnOpen {
            //상태 업데이트
            ui.setSlot(0, 2) { slot ->
                slot.item = getDecisionIcon(tradeData.acceptor.decision)
            }
            ui.setSlot(8, 2) { slot ->
                slot.item = getDecisionIcon(tradeData.requester.decision)
            }

            // READY로 상태전환
            if (viewer.decision == Decision.NOT_READY && !viewer.tradeItems.isEmpty) {
                ui.setSlot(if (tradeData.acceptor == viewer) 2 else 6, 4) {
                    it.item = confirmIcon
                    it.setOnClick {
                        viewer.decision = Decision.READY
                        viewer.player.playSound(Sounds.click)

                        //update
                        tradeData.openAll()
                    }
                }
            } else ui.removeSlot(if (tradeData.acceptor == viewer) 2 else 6, 4)

            //CONFIRM으로 상태전환
            if (tradeData.isAllReady && viewer.decision != Decision.CONFIRM) {
                ui.setSlot(4, 4) {
                    it.item = tradeIcon
                    it.setOnClick {
                        viewer.player.playSound(Sounds.click)
                        viewer.decision = Decision.CONFIRM

                        // 양쪽 모두 승낙시 거래 진행
                        if (tradeData.isAllConfirmed) {
                            tradeData.successTrade()

                            //end
                            tradeData.closeAll()
                            return@setOnClick
                        }

                        ui.setSlot(if (tradeData.acceptor == viewer) 2 else 6, 4) { slot ->
                            slot.item = ItemStack(Material.GREEN_WOOL)
                        }
                    }
                }
            }

            //거래 아이템 채우기
            repeat(9) { i ->
                val x = i % 3; val y = i / 3

                //아이템이 있는 만큼만 repeat
                if (tradeData.acceptor.getItems().size > i) {
                    ui.setSlot(x + 1, y + 1) {
                        it.item = tradeData.acceptor.getItems()[i]
                        if (tradeData.acceptor != viewer || viewer.decision != Decision.NOT_READY) return@setSlot

                        // 아이템 등록 해제
                        it.setOnClick { e ->
                            if (isNullOrAir(e.currentItem)) return@setOnClick
                            viewer.player.playSound(Sounds.click)
                            viewer.unregisterItem(x, y, e.currentItem!!, e.isShiftClick)

                            //update
                            tradeData.openAll()
                        }
                    }
                } else ui.removeSlot(x + 1, y + 1)

                //아이템이 있는 만큼만 repeat 2222
                if (tradeData.requester.getItems().size > i) {
                    ui.setSlot(x + 5, y + 1) {
                        it.item = tradeData.requester.getItems()[i]
                        if (tradeData.requester != viewer || viewer.decision != Decision.NOT_READY) return@setSlot

                        // 아이템 등록 해제
                        it.setOnClick { e ->
                            if (isNullOrAir(e.currentItem)) return@setOnClick
                            viewer.player.playSound(Sounds.click)
                            viewer.unregisterItem(x, y, e.currentItem!!, e.isShiftClick)

                            //update
                            tradeData.openAll()
                        }
                    }
                } else ui.removeSlot(x + 5, y + 1)
            }
        }
    }
}

