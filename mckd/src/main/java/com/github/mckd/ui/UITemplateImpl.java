package com.github.mckd.ui;

import com.github.mckd.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UITemplateImpl implements UITemplate {
    private final int lines;
    private final Inventory inventory;
    private final Map<Tuple<Integer, Integer>, Slot> slots = new HashMap<>();

    private Consumer<InventoryOpenEvent> onOpen;
    private Consumer<InventoryCloseEvent> onPlayerClose;
    private Consumer<InventoryCloseEvent> onPluginClose;
    private Consumer<InventoryCloseEvent> onElseClose;
    private Consumer<InventoryClickEvent> onClickBottom;

    public UITemplateImpl(Integer lines) {
        this.lines = lines;
        inventory = Bukkit.createInventory(this, lines * 9);
    }

    @Override
    public void openUI(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Slot getSlotAt(int x, int y) {
        return getSlotAt(new Tuple<>(x, y));
    }

    public Slot getSlotAt(Tuple<Integer, Integer> coordinate) {
        return slots.get(coordinate);
    }

    @Override
    public void setSlot(int x, int y, @NotNull Consumer<Slot> consumer) {
        SlotImpl slot = new SlotImpl(x, y);
        consumer.accept(slot);
        inventory.setItem(x + y * 9, slot.getItem());
        slots.put(new Tuple<>(x, y), slot);
    }

    @Override
    public int getLines() {
        return lines;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public Consumer<InventoryOpenEvent> getOnOpen() {
        return onOpen;
    }

    @Override
    public void setOnOpen(Consumer<InventoryOpenEvent> onOpen) {
        this.onOpen = onOpen;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnPlayerClose() {
        return onPlayerClose;
    }

    @Override
    public void setOnPlayerClose(Consumer<InventoryCloseEvent> onPlayerClose) {
        this.onPlayerClose = onPlayerClose;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnPluginClose() {
        return onPluginClose;
    }

    @Override
    public void setOnPluginClose(Consumer<InventoryCloseEvent> onPluginClose) {
        this.onPluginClose = onPluginClose;
    }

    @Override
    public Consumer<InventoryCloseEvent> getOnElseClose() {
        return onElseClose;
    }

    @Override
    public void setOnElseClose(Consumer<InventoryCloseEvent> onElseClose) {
        this.onElseClose = onElseClose;
    }

    @Override
    public Consumer<InventoryClickEvent> getOnClickBottom() {
        return onClickBottom;
    }

    @Override
    public void setOnClickBottom(Consumer<InventoryClickEvent> onClickBottom) {
        this.onClickBottom = onClickBottom;
    }
}
