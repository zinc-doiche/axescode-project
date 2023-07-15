package com.github.axescode.core.ui.template;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SlotImpl implements Slot {
    private final int x;
    private final int y;
    private ItemStack item;
    private Consumer<InventoryClickEvent> onClick;

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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")={item=ItemStack(" + item.getType() + " x " + item.getAmount() + "), onClick=" + onClick + "}";
    }
}
