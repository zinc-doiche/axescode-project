package com.github.axessystem

import com.github.axescode.util.Items
import com.github.axessystem.`object`.generator.BlockGenerator
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.listener.PlayerListener
import com.github.axessystem.listener.ServerListener
import com.github.axessystem.`object`.trade.Trader
import com.github.axessystem.ui.GeneratorUI
import com.github.axessystem.ui.ItemsAdderBridge.setUI
import com.github.axessystem.ui.TradeUI
import com.github.axessystem.util.text
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class AxesSystem: JavaPlugin() {
    override fun onEnable() {
        plugin = this
        pluginScope = HeartbeatScope()

        logger.info("init axes-sys")

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
            then("name" to string()) {
                executes {
                    val name: String by it
                    player.openFrame(TradeUI.getFrame())
                    player.setUI("", "axescode:$name")
                }
            }
        }

        register("test2") {
            executes {
                PlayerListener.trader?.view() ?: kotlin.run { player.sendMessage("bb") }
            }
        }

        register("test3") {
            executes {
                pluginScope.async {
                    TradeUI.getFrame().let { frame ->
                        player.openFrame(frame)
                        frame.apply {
                            var isCancelled = false
                            onClose {
                                isCancelled = true
                            }
                            repeat(9) { i ->
                                if(isCancelled) return@repeat
                                delay(1000)
                                info(i)
                                slot(0, 0) {
                                    item = Items.item(Material.PAPER) {it.displayName(text(i.toString()))}
                                }
                            }
                        }
                    }
                }
            }
        }

        register("generator") {
            then("get") {
                val generatorArgument = dynamic(StringType.SINGLE_WORD) { _, input ->
                    BlockGenerator.allGenerators.forEach {
                        if(it.generatorName == input) return@dynamic it
                    }
                }.apply {
                    suggests {
                        suggest(BlockGenerator.allGenerators.map(BlockGenerator::generatorName))
                    }
                }
                then("generator" to generatorArgument) {
                    executes {
                        val generator: BlockGenerator by it
                        Items.giveItem(player, generator.item)
                    }
                }
            }

            then("manage") {
                executes {
                    player.openFrame(GeneratorUI.get(player))
                }
            }

            then("display") {
                requires {
                    player.isOp
                }

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

    private fun registerCommand(
        command: String,
        onTabComplete: (CommandSender, Command, String, Array<out String>) -> MutableList<String>,
        onCommand: (CommandSender, Command, String, Array<out String>) -> Boolean
    ) {
        getCommand(command)?.setExecutor(object : TabExecutor {
            override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>)
                    = onTabComplete(sender, command, label, args)
            override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>)
                    = onCommand(sender, command, label, args)
        })
    }

    private fun registerAll(vararg listeners: Listener) {
        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }
}

internal lateinit var plugin: JavaPlugin private set
internal lateinit var pluginScope: CoroutineScope private set
internal fun info(msg: Any) = plugin.logger.info(msg.toString())
internal fun warn(msg: Any) = plugin.logger.warning(msg.toString())