package com.github.axessystem.`object`.trade

import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.core.trade.TradeVO
import com.github.axessystem.util.encodedItem
import com.github.axessystem.util.useOutputStream
import com.github.axessystem.util.writeItem
import com.github.axescode.core.ui.UITemplates
import com.github.axescode.util.Items
import com.github.axessystem.pluginScope
import com.github.axessystem.util.Lockable
import kotlinx.coroutines.async

/**
 * @see [TradeData.startTrade]
 */
data class TradeData(
    val acceptor: Trader,
    val requester: Trader
): Lockable {
    private var isLocked = HashMap<String, Boolean>()
    override fun lock(key: String) { isLocked[key] = true }
    override fun isLocked(key: String): Boolean = isLocked[key] ?: false
    override fun unLock(key: String) { isLocked[key] = false }

    var tradeState = TradeState.PROCEED
        private set

    val isAllReady: Boolean
        get()= acceptor.decision == Decision.READY && requester.decision == Decision.READY

    val isAllConfirmed: Boolean
        get() = acceptor.decision == Decision.CONFIRM && requester.decision == Decision.CONFIRM

    /**
     * 거래를 시작합니다.
     */
    fun startTrade() {
        acceptor.tradeUI = TradeUI(this@TradeData, acceptor)
        requester.tradeUI = TradeUI(this@TradeData, requester)

        UITemplates.addViewer(acceptor.player)
        UITemplates.addViewer(requester.player)
        openAll()
    }

    /**
     * 거래상태를 [TradeState.DENIED]로 바꾸고 거래를 종료합니다.
     */
    fun denyTrade() {
        if(isLocked["state"] == true) return
        lock("state")

        tradeState = TradeState.DENIED
        UITemplates.removeViewer(acceptor.player)
        UITemplates.removeViewer(requester.player)
    }

    /**
     * 거래상태를 [TradeState.SUCCESS]로 바꾸고 거래를 종료합니다.
     */

    fun successTrade() {
        if(isLocked("state")) return
        lock("state")

        tradeState = TradeState.SUCCESS
        UITemplates.removeViewer(acceptor.player)
        UITemplates.removeViewer(requester.player)
    }


    //거레 취소 시 아이템 돌려주는 롤백용 서브루틴
    fun lazyRollback() {
        if(isLocked("end")) return
        lock("end")
        pluginScope.async {
            sendMessageAll("등록된 아이템을 회수합니다.")
            Items.addItem(acceptor.player, *acceptor.getItems().toTypedArray())
            Items.addItem(requester.player, *requester.getItems().toTypedArray())
        }
    }

    //저장 시 서브루틴
    fun lazySave() {
        if(isLocked("end")) return
        lock("end")
        pluginScope.async {
            sendMessageAll("거래 기록 저장 중...")
            saveData()

            sendMessageAll("거래 진행 중...")
            Items.addItem(requester.player, *acceptor.getItems().toTypedArray())
            Items.addItem(acceptor.player, *requester.getItems().toTypedArray())

            sendMessageAll("거래 완료!")
        }
    }

    fun openAll() {
        acceptor.openUI()
        requester.openUI()
    }

    fun closeAll() {
        acceptor.closeUI()
        requester.closeUI()
    }

    fun sendMessageAll(msg: String) {
        acceptor.sendMessage(msg)
        requester.sendMessage(msg)
    }

    fun saveData() {
        TradeDAO.use { dao ->
            TradeVO.builder()
                .tradeAcceptorId(acceptor.playerData.playerId)
                .tradeRequesterId(requester.playerData.playerId)
                .tradeAcceptorSentMoney(acceptor.tradeMoney)
                .tradeRequesterSentMoney(requester.tradeMoney)
                .build()
            .let { tradeVO ->
                //DB 저장
                dao.save(tradeVO)

                //ItemStack 직렬화
                useOutputStream { bs, os ->
                    acceptor.getItems().map { item ->
                        os.writeItem(item)
                        TradeItemVO.builder()
                            .tradeId(tradeVO.tradeId)
                            .playerId(acceptor.playerData.playerId)
                            .tradeItem(bs.encodedItem)
                            .build()
                    }.forEach(dao::saveItem) // 각각 저장
                }
            }
        }
    }
}

/**
 * 거래의 진행 상태를 나타냅니다.
 * @see [TradeData]
 */
enum class TradeState {
    /**
     * 거래 진행 중인 상태입니다.
     */
    PROCEED,
    /**
     * 거래가 거절된 상태입니다.
     */
    DENIED,
    /**
     * 거래가 성공한 상태입니다.
     */
    SUCCESS,
    ;
}