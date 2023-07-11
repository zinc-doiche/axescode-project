package com.github.mckd.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface Slot {
    Integer getX();
    Integer getY();

    ItemStack getItem();
    void setItem(ItemStack item);

    Consumer<InventoryClickEvent> getOnClick();
    void setOnClick(Consumer<InventoryClickEvent> consumer);
}
