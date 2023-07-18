package com.github.axescode.core.ui.template;

import com.github.axescode.core.ui.slot.SquareSlot;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * {@link SquareSlot}을 통한 칸 하나하나의 {@link InventoryClickEvent} 등록이 가능한 {@link UI}를 나타냅니다.
 */
public interface SquareUI extends UI{
    SquareSlot getSlotAt(int x, int y);
    void setSlot(int x, int y, Consumer<SquareSlot> consumer);
    void removeSlot(int x, int y);
    int getLines();
}
