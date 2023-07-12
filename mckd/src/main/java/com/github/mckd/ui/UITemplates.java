package com.github.mckd.ui;

import com.github.mckd.AxesSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UITemplates {
    private static final Map<String, UITemplate> uiContainer;

    static {
        uiContainer = new HashMap<>();
    }

    public static UITemplate getUI(String key) {
        return uiContainer.get(key);
    }

    public static void init() {
        registerUI("test-ui", 3, ui -> {
            ui.setOnOpen(e -> e.getPlayer().sendMessage("opened"));
            ui.setOnElseClose(e -> {
                e.getPlayer().sendMessage("cannot close");
                AxesSystem.inst().getServer().getScheduler().runTaskLater(AxesSystem.inst(),
                        () -> ui.openUI((Player) e.getPlayer()), 1L);
            });
            ui.setOnPlayerClose(e -> {
                e.getPlayer().sendMessage("cannot close");
                AxesSystem.inst().getServer().getScheduler().runTaskLater(AxesSystem.inst(),
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

    public static List<String> getAllUINames() {
        return uiContainer.keySet().stream().toList();
    }

    public static void registerUI(String key, int lines, Consumer<UITemplate> consumer) {
        UITemplateImpl ui = new UITemplateImpl(lines);
        consumer.accept(ui);
        uiContainer.put(key, ui);
    }
}
