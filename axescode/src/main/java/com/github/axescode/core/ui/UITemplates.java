package com.github.axescode.core.ui;

import com.github.axescode.core.ui.template.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;
import java.util.function.Consumer;

/**
 * {@link UI}을 다루는 {@code Manager Class}입니다.
 */
public class UITemplates {
    private static final Set<Player> uiViewers;

    static {
        uiViewers = new HashSet<>();
    }

    // 프라이빗 접근 제한
    private UITemplates() {;}

    /**
     * {@link SquareUI}을 구현합니다.
     *
     * @param lines y축 슬롯 개수
     * @param consumer 초기 설정
     * @return 만들어진 {@link SquareUI}
     */
    public static SquareUI createSquareUI(int lines, Consumer<SquareUI> consumer) {
        SquareUIImpl ui = new SquareUIImpl(lines);
        consumer.accept(ui);
        return ui;
    }

    /**
     * {@link SquareUI}을 구현합니다.
     *
     * @param lines y축 슬롯 개수
     * @param consumer 인벤토리 타이틀
     * @param title 초기 설정
     * @return 만들어진 {@link SquareUI}
     */
    public static SquareUI createSquareUI(int lines, Component title, Consumer<SquareUI> consumer) {
        SquareUIImpl ui = new SquareUIImpl(lines, title);
        consumer.accept(ui);
        return ui;
    }

    /**
     * {@link DynamicUI}을 구현합니다.
     *
     * @param inventoryType 인벤토리 타입
     * @param consumer 초기 설정
     * @return 만들어진 {@link DynamicUI}
     */
    public static DynamicUI createDynamicUI(InventoryType inventoryType, Consumer<DynamicUI> consumer) {
        DynamicUIImpl ui = new DynamicUIImpl(inventoryType);
        consumer.accept(ui);
        return ui;
    }

    /**
     * {@link DynamicUI}을 구현합니다.
     *
     * @param inventoryType 인벤토리 타입
     * @param title 인벤토리 타이틀
     * @param consumer 초기 설정
     * @return 만들어진 {@link DynamicUI}
     */
    public static DynamicUI createDynamicUI(InventoryType inventoryType, Component title, Consumer<DynamicUI> consumer) {
        DynamicUIImpl ui = new DynamicUIImpl(inventoryType, title);
        consumer.accept(ui);
        return ui;
    }

    /**
     * {@link PlayerMoveEvent}를 막을 {@link Player}를 추가
     * @param player UI를 보고 있는 {@link Player}를 나타냄.
     */
    public static void addViewer(Player player) {
        uiViewers.add(player);
    }

    /**
     * 해당 {@link Player}가 UI를 보고 있는지 (내부적으로 {@link Set} 안에 있는지) 검사함
     * @param player UI를 보고 있는 {@link Player}를 나타냄.
     */
    public static boolean isViewer(Player player) {
        return uiViewers.contains(player);
    }

    /**
     * 해당 {@link Player}의 {@link PlayerMoveEvent} 캔슬 해제
     * @param player UI를 보고 있는 {@link Player}를 나타냄.
     */
    public static void removeViewer(Player player) {
        uiViewers.remove(player);
    }
}
