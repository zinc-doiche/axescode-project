package com.github.axessystem.generator

import com.github.axessystem.plugin
import com.github.axessystem.warn
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.Location
import org.bukkit.entity.BlockDisplay

data class BlockGeneratorData(
    val id: Long,
    val location: Location,
    val generator: BlockGenerator,
    var isCooldown: Boolean = false
) {
    companion object {
        private val placedGenerators = HashMap<Location, BlockGeneratorData>()
        val fakeServer: FakeEntityServer = FakeEntityServer.create(plugin)

        val getAllData: List<BlockGeneratorData>
            get() = placedGenerators.values.toList()

        operator fun set(location: Location, blockGeneratorData: BlockGeneratorData) {
            placedGenerators[location] = blockGeneratorData

            fakeServer.spawnEntity(blockGeneratorData.location, BlockDisplay::class.java).updateMetadata {
                block = blockGeneratorData.generator.display.createBlockData()
                isGlowing = true
            }

            fakeServer.update()
        }

        operator fun get(location: Location): BlockGeneratorData? = placedGenerators[location]

        fun remove(location: Location) {
            placedGenerators.remove(location)
            fakeServer.entities.find { it.location == location }?.remove() ?: kotlin.run { warn("${location.toVector()}에 있는 디스플레이 삭제 실패!") }
            fakeServer.update()
        }
    }
}