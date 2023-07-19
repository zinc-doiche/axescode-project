package com.github.axescode.inventory.template;

import com.github.axescode.inventory.handler.Viewer;
import com.github.axescode.inventory.ui.DynamicUI;
import com.github.axescode.inventory.handler.UIHandler;
import com.github.axescode.inventory.UITemplates;
import com.github.axescode.inventory.ui.UI;
import com.github.axescode.util.Items;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class InputUI implements UIHandler {
    private final DynamicUI ui;
    private final Viewer viewer;
    private final UIHandler parentUI;
    private String content = "";

    private static final ItemStack cancel = Items.item(Material.PAPER, Component.text("cancel"));
    private static final ItemStack confirm = Items.item(Material.PAPER, Component.text("confirm"));
    private static final ItemStack defaultText = Items.item(Material.PAPER, Component.text(""));

    public InputUI(Viewer viewer, UIHandler parentUI) {
        this.viewer = viewer;
        this.parentUI = parentUI;

        //ui init
        ui = UITemplates.createDynamicUI(InventoryType.ANVIL, ui -> {
            ui.setSlot(0, slot -> slot.setItem(defaultText));
            ui.setSlot(1, slot -> slot.setItem(cancel));
            ui.setSlot(2, slot -> slot.setItem(confirm));
        });

        ui.getSlot(0).getItem().editMeta(meta -> meta.displayName(Component.text(content)));
        //cancel
        ui.getSlot(1).setOnClick(e -> parentUI.openUI());
        //confirm
        ui.getSlot(2).setOnClick(e -> {
            AnvilInventory inv = (AnvilInventory) e.getClickedInventory();
            if(inv == null) return;
            content = inv.getRenameText();
            parentUI.openUI();
        });
    }

    /**
     * 기본 설정된 {@link DynamicUI}를 오픈합니다.
     * @see UI#openUI(Player)
     */
    @Override
    public void openUI() {
        ui.openUI(viewer.getPlayer());
    }
}
