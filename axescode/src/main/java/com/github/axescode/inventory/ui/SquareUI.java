package com.github.axescode.inventory.ui;

import com.github.axescode.inventory.slot.SquareSlot;
import com.github.axescode.util.Extension;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * {@link SquareSlot}을 통한 칸 하나하나의 {@link InventoryClickEvent} 등록이 가능한 {@link UI}를 나타냅니다.
 */
public interface SquareUI extends UI, Extension<SquareUI> {
    SquareSlot getSlotAt(int x, int y);
    void setSlot(int x, int y, Consumer<SquareSlot> consumer);
    void removeSlot(int x, int y);
    int getLines();
    SquareUI clone();
}
