package com.github.axescode.inventory.ui;

import com.github.axescode.inventory.slot.Slot;
import com.github.axescode.util.Extension;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

/**
 * 비정형 {@link Inventory}를 가지는 {@link UI}를 나타냅니다.
 */
public interface DynamicUI extends UI, Extension<DynamicUI> {
    InventoryType getInventoryType();
    int getLastSlot();

    Slot getSlot(int slot);
    void setSlot(int slot, Consumer<Slot> setSlot);
    void removeSlot(int slot);

    DynamicUI clone();
}
