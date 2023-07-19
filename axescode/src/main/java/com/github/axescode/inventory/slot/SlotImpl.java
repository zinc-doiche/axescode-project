package com.github.axescode.inventory.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SlotImpl implements Slot {
    private ItemStack item;
    private Consumer<InventoryClickEvent> onClick;

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public Consumer<InventoryClickEvent> getOnClick() {
        return onClick;
    }

    @Override
    public void setOnClick(Consumer<InventoryClickEvent> consumer) {
        this.onClick = consumer;
    }
}
