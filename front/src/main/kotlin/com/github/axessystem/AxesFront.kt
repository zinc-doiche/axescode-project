package com.github.axessystem

import com.github.axescode.container.Containers
import com.github.axescode.core.player.PlayerData
import com.github.axescode.core.trade.TradeDAO
import com.github.axescode.core.trade.TradeItemVO
import com.github.axescode.util.Items
import com.github.axessystem.`object`.generator.BlockGenerator
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.listener.PlayerListener
import com.github.axessystem.listener.ServerListener
import com.github.axessystem.`object`.trade.TradeData
import com.github.axessystem.`object`.trade.Trader
import com.github.axessystem.ui.GeneratorUI
import com.github.axessystem.util.*
import com.github.axessystem.util.useOutputStream
import com.github.axessystem.util.writeItem
import com.github.axescode.core.ui.template.UITemplate
import com.github.axescode.core.ui.UITemplates
import com.github.axessystem.`object`.generator.GeneratorViewer
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class AxesFront: JavaPlugin() {
    override fun onEnable() {
        plugin = this
        pluginScope = HeartbeatScope()

        logger.info("Axescode Front Init")
        BlockGenerator.apply {
            configInit()
            read()
        }

        registerAll(
            PlayerListener(),
            ServerListener()
        )

        kommand {
        register("test") {
            then("save") {
                executes {
                    pluginScope.async {
                        TradeDAO.use { dao ->
                            useOutputStream { bs, os ->
                                repeat(10) { i ->
                                    player.sendMessage(i.toString())
                                    os.writeItem(player.inventory.itemInMainHand)
                                    TradeItemVO.builder()
                                        .playerId(Containers.getPlayerDataContainer().getData(player.name).playerId)
                                        .tradeId(1)
                                        .tradeItem(bs.encodedItem)
                                        .build()
                                    .let(dao::saveItem)
                                }
                            }
                        }
                    }
                }
            }
            then("read") {
                executes {
                    pluginScope.async {
                        TradeDAO.use { dao ->
                            Items.addItem(player, *dao.findAllById(1).map { Base64.getDecoder().decodeAsItem(it.tradeItem)!! }.toTypedArray())
                        }
                    }
                }
            }
        }

        register("openui") {
            val uiArgument = dynamic(StringType.SINGLE_WORD) { _, input -> UITemplates.getUI(input) }.apply {
                suggests { suggest(UITemplates.getAllUINames()) }
            }
            then("ui" to uiArgument) {
                executes {
                    val ui: UITemplate by it
                    ui.openUI(player)
                }
            }
        }

        register("trade") {
            val traderArg = dynamic(StringType.SINGLE_WORD) { _, input ->
                Trader(Containers.getPlayerDataContainer().getData(input))
            }.apply {
                suggests { suggest(Containers.getPlayerDataContainer().all.map(PlayerData::getPlayerName)) }
            }
            then("acceptor" to traderArg) {
                then("requester" to traderArg) {
                    executes {
                        val acceptor: Trader by it
                        val requester: Trader by it

                        val data = TradeData(acceptor, requester)
                        data.startTrade()
                    }
                }
            }
        }

        register("axesdebug") {
            requires { player.isOp }
            executes { player.sendMessage(player.inventory.itemInMainHand.toString()) }
        }

        register("generator") {
            then("get") {
                val generatorArgument = dynamic(StringType.SINGLE_WORD) { _, input ->
                    BlockGenerator.allGenerators.forEach {
                        if(it.generatorName == input) return@dynamic it
                    }
                }.apply {
                    suggests { suggest(BlockGenerator.allGenerators.map(BlockGenerator::generatorName)) }
                }
                then("generator" to generatorArgument) {
                    executes {
                        val generator: BlockGenerator by it
                        Items.addItem(player, generator.item)
                    }
                }
            }
            then("manage") {
                executes {
                    val viewer = GeneratorViewer(player)
                    GeneratorUI(viewer).openUI()
                }
            }
            then("display") {
                requires { player.isOp }
                then("on") {
                    executes {
                        BlockGeneratorData.fakeServer.addPlayer(player)
                        BlockGeneratorData.fakeServer.update()
                    }
                }
                then("off") {
                    executes {
                        BlockGeneratorData.fakeServer.removePlayer(player)
                    }
                }
            }
        }
        }
    }

    private fun registerAll(vararg listeners: Listener) {
        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }
}

internal lateinit var plugin: JavaPlugin private set
internal lateinit var pluginScope: CoroutineScope private set
internal fun info(msg: Any) = plugin.logger.info(msg.toString())
internal fun warn(msg: Any) = plugin.logger.warning(msg.toString())