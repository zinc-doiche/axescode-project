package com.github.mckd.listener;

import com.github.mckd.ui.Slot;
import com.github.mckd.ui.UITemplate;
import com.github.mckd.ui.UITemplates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

public class UIListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        UITemplate ui; Slot slot;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UITemplate)) return;
        // 지정한 UI가 맞으면 아이템 고정
        event.setCancelled(true);

        ui = (UITemplate) inventory.getHolder();

        if(ui.getLines() * 9 <= event.getSlot()) {
            // Bottom Click
            if(ui.getOnClickBottom() == null) return;
            ui.getOnClickBottom().accept(event);
        } else {
            // UI Click
            slot = ui.getSlotAt(event.getSlot() % 9, event.getSlot() / 8);
            if(slot == null || slot.getOnClick() == null) return;
            slot.getOnClick().accept(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        UITemplate ui;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UITemplate)) return;
        ui = (UITemplate) inventory.getHolder();

        if(ui.getOnOpen() == null) return;
        ui.getOnOpen().accept(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        UITemplate ui;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UITemplate)) return;
        ui = (UITemplate) inventory.getHolder();

        switch (event.getReason()) {
            case PLAYER -> {
                if(ui.getOnPlayerClose() == null) return;
                ui.getOnPlayerClose().accept(event);
            }
            case PLUGIN -> {
                if(ui.getOnPluginClose() == null) return;
                ui.getOnPluginClose().accept(event);
            }
            case OPEN_NEW -> {;}
            default -> {
                if(ui.getOnElseClose() == null) return;
                ui.getOnElseClose().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCancelTrade(PlayerMoveEvent e) {
        //UI 닫고 움직이는 것 방지
        if(UITemplates.isViewer(e.getPlayer())) e.setCancelled(true);
    }
}