package com.github.axessystem

import com.github.axescode.container.Containers
import com.github.axescode.core.player.PlayerData
import com.github.axescode.inventory.UITemplates
import com.github.axescode.util.Items
import com.github.axessystem.`object`.generator.BlockGenerator
import com.github.axessystem.listener.PlayerListener
import com.github.axessystem.listener.ServerListener
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.`object`.generator.GeneratorUI
import com.github.axessystem.`object`.trade.TradeData
import com.github.axessystem.`object`.trade.Trader
import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.CoroutineScope
import org.bukkit.Material
import org.bukkit.command.*
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil

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

        register("showui", { player, _, _, args ->
            val tabs = Tabs(args)
            when(args.size) {
                1 -> tabs.get(0, "axescode:")
                2 -> tabs.get(1, "<lines>")
                3 -> tabs.get(2, "<dy>")
                4 -> tabs.get(3, "<dx>")
                else -> tabs.default
            }
        }, { player, _, _, args ->
            if(player !is Player) return@register false
            val lines = if(args.size > 1) args[1].toInt() else 6
            val dy = if(args.size > 2) args[2].toInt() else 16
            val dx = if(args.size > 3) args[3].toInt() else -8

            UITemplates.createSquareUI(lines) { ui ->
                repeat(45) { i ->
                    val x = i % 9
                    val y = i / 9

                    ui.setSlot(x, y) {
                        it.item = ItemStack(Material.PAPER)
                    }
                }
            }.openUI(player)
            TexturedInventoryWrapper.setPlayerInventoryTexture(player, FontImageWrapper(args[0]), "", dy, dx)
            true
        })

        register("trade", { _, _, _, args ->
            val list = mutableListOf<String>()
            Containers.getPlayerDataContainer().let { con ->
                val tabs = Tabs(args)
                when(args.size) {
                    1 -> tabs.get(0, *con.all.map(PlayerData::getPlayerName).toTypedArray())
                    2 -> tabs.get(1, *con.all.map(PlayerData::getPlayerName).toTypedArray())
                    else -> list
                }
            }
        },
        { _, _, _, args ->
            Containers.getPlayerDataContainer().let {
                if (args.size == 2 && it.hasData(args[0]) && it.hasData(args[1])) {
                    val requester = Trader(it.getData(args[0]))
                    val acceptor = Trader(it.getData(args[1]))

                    TradeData(acceptor, requester).startTrade()
                }
            }
            true
        })
        register("generator", { _, _, _, args ->
            val tabs = Tabs(args)
            when(args.size) {
                1 -> tabs.get(0, "get", "manage", "display")
                2 -> when(args[0]) {
                    "get" -> tabs.get(1, *BlockGenerator.allGenerators.map(BlockGenerator::generatorName).toTypedArray())
                    "display" -> tabs.get(1, "on", "off")
                    else -> tabs.default
                }
                else -> tabs.default
            }
        },
        { player, _, _, args ->
            if(player !is Player) return@register false
            when(args.size) {
                1 -> when(args[0]) {
                    "manage" -> {
                        GeneratorUI(player).openUI()
                    }
                }
                2 -> when(args[0]) {
                    "get" -> BlockGenerator.allGenerators.find { it.generatorName == args[1] }?.let {
                        Items.addItem(player, it.item)
                    }
                    "display" -> when(args[1]) {
                        "on" -> {
                            BlockGeneratorData.fakeServer.addPlayer(player)
                            BlockGeneratorData.fakeServer.update()
                        }
                        "off" -> BlockGeneratorData.fakeServer.removePlayer(player)
                    }
                }
            }
            true
        })
//        kommand {
//        register("test") {
//            then("save") {
//                executes {
//                    pluginScope.async {
//                        TradeDAO.use { dao ->
//                            useOutputStream { bs, os ->
//                                repeat(10) { i ->
//                                    playerEntity.sendMessage(i.toString())
//                                    os.writeItem(playerEntity.inventory.itemInMainHand)
//                                    TradeItemVO.builder()
//                                        .playerId(Containers.getPlayerDataContainer().getData(playerEntity.name).playerId)
//                                        .tradeId(1)
//                                        .tradeItem(bs.encodedItem)
//                                        .build()
//                                    .let(dao::saveItem)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            then("read") {
//                executes {
//                    pluginScope.async {
//                        TradeDAO.use { dao ->
//                            Items.addItem(playerEntity, *dao.findAllById(1).map { Base64.getDecoder().decodeAsItem(it.tradeItem)!! }.toTypedArray())
//                        }
//                    }
//                }
//            }
//        }
//        register("axesdebug") {
//            requires { playerEntity.isOp }
//            executes { playerEntity.sendMessage(playerEntity.inventory.itemInMainHand.toString()) }
//        }
//        }
    }

    private fun registerAll(vararg listeners: Listener) {
        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }

    private fun register(command: String, commandExecutor: (player: CommandSender, command: Command, label: String, args: Array<out String>) -> Boolean) {
        getCommand(command)?.setExecutor { sender, commandObj, label, args -> commandExecutor(sender, commandObj, label, args) }
    }

    private fun register(
        command: String,
        tabCompleter: (player: CommandSender, command: Command, label: String, args: Array<out String>) -> MutableList<String>,
        commandExecutor: (player: CommandSender, command: Command, label: String, args: Array<out String>) -> Boolean
    ) {
        getCommand(command)?.setExecutor(object : TabExecutor {
            override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>
                = tabCompleter(sender, command, label, args)
            override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
                = commandExecutor(sender, command, label, args)
        })
    }
}

internal lateinit var plugin: JavaPlugin private set
internal lateinit var pluginScope: CoroutineScope private set
internal fun info(msg: Any) = plugin.logger.info(msg.toString())
internal fun warn(msg: Any) = plugin.logger.warning(msg.toString())

class Tabs(private val args: Array<out String>) {
    fun get(idx: Int, vararg suggestions: String): MutableList<String>
        = StringUtil.copyPartialMatches(args[idx], suggestions.toList(), default)
    val default = mutableListOf<String>()
}
