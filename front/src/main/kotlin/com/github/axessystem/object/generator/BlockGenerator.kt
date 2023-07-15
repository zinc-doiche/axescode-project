package com.github.axessystem.`object`.generator

import com.github.axescode.util.Items.item
import com.github.axessystem.plugin
import com.github.axessystem.warn
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.stream.Collectors
import kotlin.collections.ArrayList

import com.github.axescode.util.Colors
import com.github.axescode.util.Items
import com.github.axessystem.info
import com.github.axessystem.pluginScope
import com.github.axessystem.util.*
import com.github.axessystem.util.text
import com.github.axessystem.util.texts
import com.github.axessystem.util.ticksToMillis
import kotlinx.coroutines.*
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.metadata.FixedMetadataValue
import java.text.DecimalFormat

class BlockGenerator private constructor(
    val generatorName: String,
    val display: Material,
    private val weightedMaterialList: List<Material>,
    private val cooldownDisplay: Material,
    private val cooldownTicks: Long
): Placeable {
    /**
     * 중복 제거된 [Material] 리스트
     */
    val materials: List<Material>
        get() = weightedMaterialList.stream().distinct().collect(Collectors.toList())

    /**
     * 해당 함수를 통하여 결과 도출
     */
    val randomMaterial: Material
        get() = weightedMaterialList.random()

    /**
     * 설치 가능한 생성기 아이템
     */
    val item: ItemStack get() = item(display) { meta ->
        meta.displayName(text(generatorName).color(Colors.gold))
        meta.lore(texts(text("드롭 아이템 목록: ").color(Colors.green).decoration(TextDecoration.BOLD, true)).toMutableList().apply {
            materials.forEach { material ->
                val count = weightedMaterialList.count {it.name == material.name}
                val probability = count / weightedMaterialList.size.toDouble() * 100
                val formattedProbability = DecimalFormat("#.##").format(probability)
                add(text(" - ").append(ItemStack(material).displayName()).append(text(": $formattedProbability%")).color(Colors.grey))
            }
        })

        Items.setPersistent(meta, Placeable.placeableKey, generatorName)
    }

    fun startCooldown(data: BlockGeneratorData, location: Location) {
        location.block.blockData = cooldownDisplay.createBlockData()
        data.isCooldown = true

        pluginScope.async {
            delay(cooldownTicks.ticksToMillis())
            location.block.blockData = display.createBlockData()
            data.isCooldown = false
        }
    }

    override fun onPlace(location: Location) {
        location.block.setMetadata(Placeable.PLACEABLE_KEY, FixedMetadataValue(plugin, generatorName))
    }

    override fun toString(): String {
        return "BlockGenerator(generatorName='$generatorName', display=$display, weightedMaterialList=$weightedMaterialList)"
    }

    //Configurable impl
    companion object: Configurable {

        /**
         * 모든 종류(Config에 등록된)의 생성기를 저장함
         */
        val allGenerators = ArrayList<BlockGenerator>()

        override val configFile: File = File(plugin.dataFolder, "generator/block-generator.yml")
        override var config: YamlConfiguration? = null

        operator fun get(name: String): BlockGenerator? = allGenerators.find { it.generatorName == name }

        override fun configInit() {
            config = YamlConfiguration.loadConfiguration(configFile)
            config?.let { config ->
                config.addDefaults(
                    mapOf<String, Any>(
                        "example-generator"
                            to mapOf(
                                "drops"
                                    to mapOf<String, Any>(
                                        "COBBLESTONE" to 4,
                                        "DIAMOND" to 1
                                    ),
                                "display" to "STONE",
                                "cooldown"
                                    to mapOf<String, Any>(
                                        "ticks" to 100,
                                        "display" to "COBBLESTONE"
                                    )
                            )
                    )
                )
                config.options().copyDefaults(true)
                config.save(configFile)
            }
        }

        override fun read() {
            try {
                config?.let { config ->

                    //각 generator 생성
                    config.getKeys(false).forEach { key ->
                        val settings = config.getConfigurationSection(key)?.getValues(false) ?: return@forEach
                        val drops = (settings["drops"] as ConfigurationSection).getValues(false)
                        val display = Material.valueOf(settings["display"] as String)
                        val cooldown = (settings["cooldown"] as ConfigurationSection).getValues(false)

                        val ticks = cooldown["ticks"] as Int
                        val cooldownDisplay = Material.valueOf(cooldown["display"] as String)

                        val materialList = ArrayList<Material>()

                        //비었으면 random 시 오류나므로 리턴
                        if(drops.isEmpty()) return@forEach

                        //각 material을 weight만큼 저장
                        drops.entries.forEach { entry ->
                            val material = Material.valueOf(entry.key as String)
                            val weight = entry.value as Int

                            repeat(weight) { materialList.add(material) }
                        }
                        val generator = BlockGenerator(key, display, materialList, cooldownDisplay, ticks.toLong())
                        allGenerators.add(generator)
                        info("생성기 \'${generator.generatorName}\' 등록중...")
                    }
                }
            } catch (e: Exception) {
                warn("YML 형식이 잘못되었습니다.")
                e.printStackTrace()
            }
        }

        override fun updateConfig() {

        }
    }
}
