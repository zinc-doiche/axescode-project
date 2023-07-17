package com.github.axessystem.listener

import com.github.axescode.core.generator.PlacedGeneratorDAO
import com.github.axescode.core.generator.PlacedGeneratorVO
import com.github.axescode.util.Items
import com.github.axessystem.`object`.generator.BlockGenerator
import com.github.axessystem.`object`.generator.BlockGeneratorData
import com.github.axessystem.`object`.menu.MenuUI
import com.github.axessystem.util.Placeable
import com.github.axessystem.warn
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class PlayerListener: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onShiftFPressed(e: PlayerSwapHandItemsEvent) {
        if(e.player.isSneaking) {
            e.isCancelled = true
            MenuUI.openUI(e.player)
        }
    }

    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if(!Items.hasPersistent(e.itemInHand, Placeable.placeableKey)) return
        Items.getPersistent(e.itemInHand, Placeable.placeableKey).map(BlockGenerator::get).ifPresent { generator ->
            val location = e.block.location.toBlockLocation()

            PlacedGeneratorDAO.use { dao ->
                PlacedGeneratorVO.builder()
                    .placedGeneratorName(generator.generatorName)
                    .placedGeneratorWorld(location.world.name)
                    .placedGeneratorX(location.blockX)
                    .placedGeneratorY(location.blockY)
                    .placedGeneratorZ(location.blockZ)
                    .build()
                .let {
                    dao.save(it)
                    BlockGeneratorData[location] = BlockGeneratorData(it.placedGeneratorId, location, generator)
                }
            }

            generator.onPlace(e.block.location)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(e: BlockBreakEvent) {
        if(!e.block.hasMetadata(Placeable.PLACEABLE_KEY)) return

        val location = e.block.location.toBlockLocation()
        BlockGeneratorData[location]?.let { genData ->
            if(e.player.gameMode == GameMode.CREATIVE) {
                PlacedGeneratorDAO.use { it.remove(genData.id) }
                BlockGeneratorData.remove(genData.location)
                return
            }
            e.isCancelled = true
            if(genData.isCooldown) return
            Items.addItem(e.player, ItemStack(genData.generator.randomMaterial))
            genData.generator.startCooldown(genData, e.block.location)
        } ?: run { warn("${location.toVector()}에 있는 생성기가 부서졌거나 없습니다.") }
    }
}
