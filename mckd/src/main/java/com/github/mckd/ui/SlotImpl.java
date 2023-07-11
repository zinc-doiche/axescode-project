package com.github.mckd.ui;

import lombok.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SlotImpl implements Slot {
    private final int x;
    private final int y;
    private ItemStack item;
    private Consumer<InventoryClickEvent> onClick;

    public void onClick(Consumer<InventoryClickEvent> consumer) {
        onClick = consumer;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }

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
