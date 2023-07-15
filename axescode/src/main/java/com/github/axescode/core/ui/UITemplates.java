package com.github.axescode.core.ui;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.core.ui.template.UITemplate;
import com.github.axescode.core.ui.template.UITemplateImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
 * {@link UITemplate}을 다루는 {@code Manager Class}입니다.
 */
public class UITemplates {
    private static final Map<String, UITemplate> uiContainer;
    private static final Set<Player> uiViewers;

    static {
        uiContainer = new HashMap<>();
        uiViewers = new HashSet<>();
    }

    // 프라이빗 접근 제한
    private UITemplates() {;}

    public static void init() {
        registerUI("test-ui", 3, ui -> {
            ui.setOnOpen(e -> e.getPlayer().sendMessage("opened"));
            ui.setOnElseClose(e -> {
                e.getPlayer().sendMessage("cannot close");
                AxescodePlugin.inst().getServer().getScheduler().runTaskLater(AxescodePlugin.inst(),
                        () -> ui.openUI((Player) e.getPlayer()), 1L);
            });
            ui.setOnPlayerClose(e -> {
                e.getPlayer().sendMessage("cannot close");
                AxescodePlugin.inst().getServer().getScheduler().runTaskLater(AxescodePlugin.inst(),
                        () -> ui.openUI((Player) e.getPlayer()), 1L);
            });

            ui.setSlot(4, 1, slot -> {
                slot.setItem(new ItemStack(Material.PAPER));
                slot.setOnClick(e -> {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("escaped!");
                });
            });
        });
    }

    public static UITemplate getUI(String key) {
        return uiContainer.get(key);
    }

    public static List<String> getAllUINames() {
        return uiContainer.keySet().stream().toList();
    }

    public static void registerUI(String key, int lines, Consumer<UITemplate> consumer) {
        UITemplateImpl ui = new UITemplateImpl(lines);
        consumer.accept(ui);
        uiContainer.put(key, ui);
    }

    public static UITemplate createUI(int lines, Consumer<UITemplate> consumer) {
        UITemplateImpl ui = new UITemplateImpl(lines);
        consumer.accept(ui);
        return ui;
    }

    public static UITemplate createUI(int lines, Component title, Consumer<UITemplate> consumer) {
        UITemplateImpl ui = new UITemplateImpl(lines, title);
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
