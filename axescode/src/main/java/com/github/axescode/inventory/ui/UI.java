package com.github.axescode.inventory.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

/**
 * 이벤트를 구현 가능한 {@link Inventory}를 가지는 {@link InventoryHolder}를 나타냅니다.
 * @see InventoryHolder
 */
public interface UI extends InventoryHolder {
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

    UI clone();
}
