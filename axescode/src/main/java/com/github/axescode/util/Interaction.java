package com.github.axescode.util;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import java.util.Objects;

import static com.github.axescode.util.Items.isFull;
import static com.github.axescode.util.Items.isNullOrAir;

/**
 * [GET] 아이템을 가져갈 때: inv!!, cursor == null or air
 *
 * [EXCHANGE] 아이템을 맞바꿀 때: inv!!, cursor!!
 *
 * [PUT] 아이템을 놓을 때: inv == null or air, cursor!!
 */
enum Interaction {
    GET,
    EXCHANGE,
    PUT;

    public static @Nullable Interaction get(InventoryView view, int slot) {
        boolean checkCursor = isNullOrAir(view.getCursor());

        return isNullOrAir(view.getItem(slot))
                ? (checkCursor ? null : PUT)
                : (checkCursor ? GET : EXCHANGE);
    }

    /**
     * called in the [InventoryClickEvent], executes one interaction among [Interaction].
     * this method will force interactions not previously done.
     */
    public static void doInteraction(Interaction interaction, InventoryView view, int slot, Boolean isShiftClick) {
        if(isShiftClick && interaction != Interaction.PUT) {
            if (isFull(view.getBottomInventory())) return;
            ItemStack item = Objects.requireNonNull(view.getItem(slot));
            view.getBottomInventory().addItem(item);
        } else switch (interaction) {
            case EXCHANGE -> {
                ItemStack temp = view.getCursor();
                view.setCursor(view.getItem(slot));
                view.setItem(slot, temp);
            }
            case PUT -> view.setItem(slot, view.getCursor());
            case GET -> view.setCursor(view.getItem(slot));
        }
    }

    public static void doSlotInteraction(InventoryView view, int slot, Boolean isShiftClick) {
        Interaction interaction = get(view, slot);
        doInteraction(interaction, view, slot, isShiftClick);
    }
}

