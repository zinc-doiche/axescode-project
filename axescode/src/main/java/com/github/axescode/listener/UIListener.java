package com.github.axescode.listener;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.inventory.slot.Slot;
import com.github.axescode.inventory.slot.SquareSlot;
import com.github.axescode.inventory.ui.DynamicUI;
import com.github.axescode.inventory.ui.SquareUI;
import com.github.axescode.inventory.ui.UI;
import com.github.axescode.inventory.UITemplates;
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
        UI ui; Slot slot;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UI)) return;
        // 지정한 UI가 맞으면 아이템 고정
        event.setCancelled(true);
        ui = (UI) inventory.getHolder();

        //Dynamic:
        if(ui instanceof DynamicUI dynamicUI) {
            if(event.getRawSlot() > dynamicUI.getLastSlot()) {
                // Bottom Click
                if(ui.getOnClickBottom() == null) return;
                ui.getOnClickBottom().accept(event);
                return;
            }
            // else UI Click
            slot = dynamicUI.getSlot(event.getRawSlot());

            if(slot == null || slot.getOnClick() == null) return;
            slot.getOnClick().accept(event);
            return;
        }
        //Square:
        if(ui instanceof SquareUI squareUI) {
            if (squareUI.getLines() * 9 <= event.getRawSlot()) {
                // Bottom Click
                if (ui.getOnClickBottom() == null) return;
                ui.getOnClickBottom().accept(event);
                return;
            }
            // else UI Click
            int x = event.getRawSlot() % 9, y = event.getRawSlot() / 9;
            slot = squareUI.getSlotAt(x, y);

            if (slot == null || slot.getOnClick() == null) return;
            slot.getOnClick().accept(event);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        UI ui;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UI)) return;
        ui = (UI) inventory.getHolder();

        if(ui.getOnOpen() == null) return;
        ui.getOnOpen().accept(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        UI ui;

        // 지정한 UI가 아닐 경우 제외
        if(!(inventory.getHolder() instanceof UI)) return;
        ui = (UI) inventory.getHolder();

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