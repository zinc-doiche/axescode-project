package com.github.axescode.core.ui.template;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

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
