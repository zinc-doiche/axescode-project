package com.github.axessystem.listener

import com.github.axescode.core.generator.PlacedGeneratorDAO
import com.github.axessystem.`object`.generator.BlockGenerator
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.info
import com.github.axessystem.plugin
import org.apache.commons.lang3.tuple.MutablePair
import org.bukkit.Location
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.util.Vector

class ServerListener: Listener {

    @EventHandler
    fun onLoad(e: ServerLoadEvent) {
        PlacedGeneratorDAO.use { dao ->
            val list = dao.findAll()

            list.forEach { gen ->
                val location = Location(plugin.server.getWorld(gen.placedGeneratorWorld),
                        gen.placedGeneratorX.toDouble(), gen.placedGeneratorY.toDouble(), gen.placedGeneratorZ.toDouble())
                val generator = BlockGenerator[gen.placedGeneratorName] ?: return@forEach

                BlockGeneratorData[location] = BlockGeneratorData(gen.placedGeneratorId, location.toBlockLocation(), generator)
                generator.onPlace(location)

                //info("Block Generator \'${generator.generatorName}\' has been place at (${location.toVector()}).")
            }

            info("${list.size}개의 생성기를 설치하고 있습니다...")
        }
    }
}