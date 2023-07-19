package com.github.axescode.inventory.template;

import com.github.axescode.inventory.handler.Viewer;
import com.github.axescode.inventory.ui.DynamicUI;
import com.github.axescode.inventory.handler.UIHandler;
import com.github.axescode.inventory.UITemplates;
import com.github.axescode.inventory.ui.UI;
import com.github.axescode.util.Items;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @see InputUI#getUI()
 */
public class InputUI implements UIHandler {
    private static final DynamicUI ui;
    private final Viewer viewer;
    private final DynamicUI uiClone;
    private final UIHandler parentUI;
    private String content = "";

    static {
        ItemStack cancel = Items.item(Material.PAPER, Component.text("cancel"));
        ItemStack confirm = Items.item(Material.PAPER, Component.text("confirm"));
        ItemStack defaultText = Items.item(Material.PAPER, Component.text(""));

        //ui init
        ui = UITemplates.createDynamicUI(InventoryType.ANVIL, ui -> {
            ui.setSlot(0, slot -> slot.setItem(defaultText));
            ui.setSlot(1, slot -> slot.setItem(cancel));
            ui.setSlot(2, slot -> slot.setItem(confirm));
        });
    }

    public InputUI(Viewer viewer, UIHandler parentUI) {
        this.viewer = viewer;
        this.parentUI = parentUI;

        uiClone = ui.apply(ui -> {
            AnvilInventory inv = (AnvilInventory) ui.getInventory();

            ui.getSlot(0).getItem().editMeta(meta -> meta.displayName(Component.text(content)));
            //cancel
            ui.getSlot(1).setOnClick(e -> parentUI.openUI());
            //confirm
            ui.getSlot(2).setOnClick(e -> {
                content = inv.getRenameText();
                parentUI.openUI();
            });

            return ui;
        });
    }

    /**
     * 기본 설정된 {@link DynamicUI}를 오픈합니다.
     * @see UI#openUI(Player)
     */
    @Override
    public void openUI() {
        uiClone.openUI(viewer.getPlayer());
    }

    /**
     * {@link InputUI}의 기본 템플릿 {@link DynamicUI}를 반환합니다.
     * @return 기본 {@link InputUI}의 {@code clone}
     */
    public DynamicUI getUI() {
        return uiClone;
    }
}
