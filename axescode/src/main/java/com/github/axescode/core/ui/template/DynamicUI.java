package com.github.axescode.core.ui.template;

import com.github.axescode.util.Tuple;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DynamicUI implements UI {
    private final Inventory inventory;
    private final Map<Tuple<Integer, Integer>, Slot> slots = new HashMap<>();

    private Consumer<InventoryOpenEvent> onOpen;
    private Consumer<InventoryCloseEvent> onPlayerClose;
    private Consumer<InventoryCloseEvent> onPluginClose;
    private Consumer<InventoryCloseEvent> onElseClose;
    private Consumer<InventoryClickEvent> onClickBottom;

    public DynamicUI(InventoryType inventoryType) {
        inventory = Bukkit.createInventory(this, inventoryType);
    }

    public DynamicUI(InventoryType inventoryType, Component title) {
        inventory = Bukkit.createInventory(this, inventoryType, title);
    }

    @Override
    public void openUI(Player player) {

    }

    @Override
    public Consumer<InventoryOpenEvent> getOnOpen() {
        return null;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnPlayerClose() {
        return null;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnPluginClose() {
        return null;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnElseClose() {
        return null;
    }

    @Override
    public Consumer<InventoryClickEvent> getOnClickBottom() {
        return null;
    }

    @Override
    public void setOnOpen(Consumer<InventoryOpenEvent> consumer) {

    }

    @Override
    public void setOnPlayerClose(Consumer<InventoryCloseEvent> consumer) {

    }

    @Override
    public void setOnPluginClose(Consumer<InventoryCloseEvent> consumer) {

    }

    @Override
    public void setOnElseClose(Consumer<InventoryCloseEvent> consumer) {

    }

    @Override
    public void setOnClickBottom(Consumer<InventoryClickEvent> consumer) {

    }

    @Override
    public UI clone() {
        return null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
