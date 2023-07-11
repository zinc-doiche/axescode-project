package com.github.axescode.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.Objects;

import static com.github.axescode.util.Items.isNullOrAir;

/**
 * 인벤토리에서의 아이템 상호작용을 나타냅니다.
 */
enum Interaction {

    /**
     * 아이템을 가져갈 때
     */
    GET,
    /**
     * 아이템을 서로 맞바꿀 때
     */
    EXCHANGE,
    /**
     * 아이템을 놓을 때
     */
    PUT;

    /**
     *  해당 {@link InventoryView}에서의 {@link Interaction}을 가져옵니다.
     *
     * @param view 대상 인벤토리의 {@link InventoryView}
     * @param slot 대상 인벤토리 슬롯
     * @return 동작에 적합한 {@link Interaction}. 커서와 슬롯 둘 다 아무것도 없을 시 {@code null} 반환
     */
    public static @Nullable Interaction get(InventoryView view, int slot) {
        boolean checkCursor = isNullOrAir(view.getCursor());

        return isNullOrAir(view.getItem(slot))
                ? (checkCursor ? null : PUT)
                : (checkCursor ? GET : EXCHANGE);
    }

    /**
     * 주어진 {@link Interaction}에 맞는 동작을 수행합니다.
     * 동작을 임의로 구현하여야 할 경우에만 사용하세요.
     *
     * @param interaction 동작할 {@link Interaction}
     * @param view 동작의 대상이 되는 {@link InventoryView}
     * @param slot 동작의 대상이 되는 {@code slot}
     * @param isShiftClick shift-click 여부
     */
    public static void doInteraction(@NotNull Interaction interaction, InventoryView view, int slot, Boolean isShiftClick) {
        if(isShiftClick && interaction != Interaction.PUT) {
            ItemStack item = Objects.requireNonNull(view.getItem(slot));
            Items.addItem((Player) view.getPlayer(), item);
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
        assert interaction != null;

        doInteraction(interaction, view, slot, isShiftClick);
    }
}

