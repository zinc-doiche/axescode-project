package com.github.axescode.core.ui.template;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public interface UITemplate extends InventoryHolder {
    Slot getSlotAt(int x, int y);
    void setSlot(int x, int y, Consumer<Slot> consumer);
    void removeSlot(int x, int y);
    int getLines();

    void openUI(Player player);

    Consumer<InventoryOpenEvent> getOnOpen();
    Consumer<InventoryCloseEvent> getOnPlayerClose();
    Consumer<InventoryCloseEvent> getOnPluginClose();
    Consumer<InventoryCloseEvent> getOnElseClose();
    Consumer<InventoryClickEvent> getOnClickBottom();

    void setOnOpen(Consumer<InventoryOpenEvent> consumer);
    void setOnPlayerClose(Consumer<InventoryCloseEvent> consumer);
    void setOnPluginClose(Consumer<InventoryCloseEvent> consumer);
    void setOnElseClose(Consumer<InventoryCloseEvent> consumer);
    void setOnClickBottom(Consumer<InventoryClickEvent> consumer);

    UITemplate clone();
}
