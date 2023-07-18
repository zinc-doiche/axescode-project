package com.github.axescode.core.ui.template;

import java.util.function.Consumer;

public interface SquareUI extends UI{
    Slot getSlotAt(int x, int y);
    void setSlot(int x, int y, Consumer<Slot> consumer);
    void removeSlot(int x, int y);
    int getLines();
}
