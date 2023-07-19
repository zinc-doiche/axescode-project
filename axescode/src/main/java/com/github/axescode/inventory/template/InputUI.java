package com.github.axescode.inventory.template;

import com.github.axescode.inventory.handler.Viewer;
import com.github.axescode.inventory.ui.DynamicUI;
import com.github.axescode.inventory.handler.UIHandler;
import com.github.axescode.inventory.UITemplates;
import com.github.axescode.util.Items;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@AllArgsConstructor
public class InputUI implements UIHandler {
    private static final DynamicUI ui;
    private final Viewer viewer;
    private String content = "";

    static {
        ItemStack cancel =  Items.item(Material.PAPER, Component.text("cancel"));
        ItemStack confirm =  Items.item(Material.PAPER, Component.text("confirm"));

        ui = UITemplates.createDynamicUI(InventoryType.ANVIL, ui -> {
            AnvilInventory inv = (AnvilInventory) ui.getInventory();
            inv.getRenameText();
            ui.setSlot(0, slot -> slot.setItem(
                    Items.item(Material.PAPER, Component.text(""))
            ));
            ui.setSlot(1, slot -> slot.setItem(cancel));
            ui.setSlot(2, slot -> slot.setItem(confirm));
        });
    }

    @Override
    public void openUI() {
        DynamicUI uiClone = ui.clone();

        AnvilInventory inv = (AnvilInventory) uiClone.getInventory();

        uiClone.getSlot(0).getItem().editMeta(meta -> meta.displayName(Component.text(content)));

        //cancel
        uiClone.getSlot(1).setOnClick(e -> {

        });

        //confirm
        uiClone.getSlot(2).setOnClick(e -> {
            content = inv.getRenameText();
        });

        uiClone.openUI(viewer.getPlayer());
    }

    public DynamicUI getUI() {
        DynamicUI uiClone = ui.clone();
        AnvilInventory inv = (AnvilInventory) uiClone.getInventory();

        uiClone.getSlot(0).getItem().editMeta(meta -> meta.displayName(Component.text(content)));
        //confirm
        uiClone.getSlot(2).setOnClick(e -> content = inv.getRenameText());

        return uiClone;
    }
}
