package com.github.axescode.inventory.ui;

import com.github.axescode.inventory.slot.Slot;
import com.github.axescode.inventory.slot.SlotImpl;
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

public class DynamicUIImpl implements DynamicUI, Cloneable {
    private final int lastSlot;
    private final InventoryType inventoryType;
    private final Inventory inventory;
    private final Map<Integer, Slot> slots = new HashMap<>();

    private Consumer<InventoryOpenEvent> onOpen;
    private Consumer<InventoryCloseEvent> onPlayerClose;
    private Consumer<InventoryCloseEvent> onPluginClose;
    private Consumer<InventoryCloseEvent> onElseClose;
    private Consumer<InventoryClickEvent> onClickBottom;

    public DynamicUIImpl(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        lastSlot  = inventoryType.getDefaultSize() - 1;
        inventory = Bukkit.createInventory(this, inventoryType);
    }

    public DynamicUIImpl(InventoryType inventoryType, Component title) {
        this.inventoryType = inventoryType;
        lastSlot  = inventoryType.getDefaultSize() - 1;
        inventory = Bukkit.createInventory(this, inventoryType, title);
    }

    @Override
    public void openUI(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    @Override
    public int getLastSlot() {
        return lastSlot;
    }

    @Override
    public Slot getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public void setSlot(int slot, Consumer<Slot> setSlot) {
        Slot slotImpl = new SlotImpl();
        setSlot.accept(slotImpl);
        inventory.setItem(slot, slotImpl.getItem());
        slots.put(slot, slotImpl);
    }

    @Override
    public void removeSlot(int slot) {
        slots.remove(slot);
    }

    @Override public Consumer<InventoryOpenEvent> getOnOpen() {return onOpen;}
    @Override public Consumer<InventoryCloseEvent> getOnPlayerClose() {return onPlayerClose;}
    @Override public Consumer<InventoryCloseEvent> getOnPluginClose() {return onPluginClose;}
    @Override public Consumer<InventoryCloseEvent> getOnElseClose() {return onElseClose;}
    @Override public Consumer<InventoryClickEvent> getOnClickBottom() {return onClickBottom;}

    @Override public void setOnOpen(Consumer<InventoryOpenEvent> onOpen) {this.onOpen = onOpen;}
    @Override public void setOnPlayerClose(Consumer<InventoryCloseEvent> onPlayerClose) {this.onPlayerClose = onPlayerClose;}
    @Override public void setOnPluginClose(Consumer<InventoryCloseEvent> onPluginClose) {this.onPluginClose = onPluginClose;}
    @Override public void setOnElseClose(Consumer<InventoryCloseEvent> onElseClose) {this.onElseClose = onElseClose;}
    @Override public void setOnClickBottom(Consumer<InventoryClickEvent> onClickBottom) {this.onClickBottom = onClickBottom;}

    @Override
    public DynamicUI clone() {
        try {
            return (DynamicUIImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
