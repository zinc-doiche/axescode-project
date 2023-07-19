package com.github.axescode.inventory.slot;

import com.github.axescode.inventory.ui.UI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * {@link UI}의 슬롯 하나를 나타냅니다.
 */
public interface Slot {
    ItemStack getItem();
    void setItem(ItemStack item);

    Consumer<InventoryClickEvent> getOnClick();
    void setOnClick(Consumer<InventoryClickEvent> consumer);
}
