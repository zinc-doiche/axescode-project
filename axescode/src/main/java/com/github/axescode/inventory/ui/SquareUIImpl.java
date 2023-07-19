package com.github.axescode.inventory.ui;

import com.github.axescode.inventory.slot.SquareSlot;
import com.github.axescode.inventory.slot.SquareSlotImpl;
import com.github.axescode.util.Items;
import com.github.axescode.util.Tuple;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class SquareUIImpl implements SquareUI, Cloneable {
    private final int lines;
    private final Inventory inventory;
    private final Map<Tuple<Integer, Integer>, SquareSlot> slots = new HashMap<>();

    private Consumer<InventoryOpenEvent> onOpen;
    private Consumer<InventoryCloseEvent> onPlayerClose;
    private Consumer<InventoryCloseEvent> onPluginClose;
    private Consumer<InventoryCloseEvent> onElseClose;
    private Consumer<InventoryClickEvent> onClickBottom;

    public SquareUIImpl(Integer lines) {
        this.lines = lines;
        inventory = Bukkit.createInventory(this, lines * 9);
    }

    public SquareUIImpl(Integer lines, Component title) {
        this.lines = lines;
        inventory = Bukkit.createInventory(this, lines * 9, title);
    }

    @Override
    public void openUI(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public SquareSlot getSlotAt(int x, int y) {
        return getSlotAt(new Tuple<>(x, y));
    }

    public SquareSlot getSlotAt(Tuple<Integer, Integer> coordinate) {
        return slots.get(coordinate);
    }

    public List<SquareSlot> getSlots() {
        return slots.values().stream().toList();
    }

    @Override
    public void setSlot(int x, int y, @NotNull Consumer<SquareSlot> consumer) {
        SquareSlotImpl slot = new SquareSlotImpl(x, y);
        consumer.accept(slot);
        inventory.setItem(x + y * 9, slot.getItem());
        slots.put(new Tuple<>(x, y), slot);
    }

    @Override
    public void removeSlot(int x, int y) {
        inventory.setItem(x + y * 9, Items.AIR);
        slots.remove(new Tuple<>(x, y));
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

    @Override
    public SquareUIImpl clone() {
        try {
            return (SquareUIImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SquareUI apply(Function<SquareUI, SquareUI> function) {
        return function.apply(clone());
    }

    @Override
    public void apply(Consumer<SquareUI> consumer) {
        consumer.accept(clone());
    }
}
