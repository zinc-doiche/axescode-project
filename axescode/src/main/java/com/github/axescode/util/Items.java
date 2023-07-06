package com.github.axescode.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class Items {
    public static final ItemStack AIR = new ItemStack(Material.AIR);

    public static ItemStack item(Material material, Consumer<ItemMeta> block) {
        ItemStack item = new ItemStack(material);
        item.editMeta(block);
        return item;
    }

    public static ItemStack item(Material material, Component name,  Consumer<ItemMeta> block) {
        return item(material, meta -> {
            meta.displayName(name);
            block.accept(meta);
        });
    }

    public static ItemStack getCustomItem(Material material, Component name, int customModelNumber, Consumer<ItemMeta> init) {
        return item(material, name, meta -> {
            meta.setCustomModelData(customModelNumber);
            init.accept(meta);
        });
    }

    public static Boolean isNullOrAir(@Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static Optional<String> getPersistent(ItemStack item, NamespacedKey key) {
        return getPersistent(item.getItemMeta(), key);
    }

    public static <T, Z> Optional<Z> getPersistent(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type) {
        return getPersistent(item.getItemMeta(), key, type);
    }

    public static Optional<String> getPersistent(ItemMeta meta, NamespacedKey key) {
        return Optional.ofNullable(meta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
    }

    public static <T, Z> Optional<Z> getPersistent(ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type) {
        return Optional.ofNullable(meta.getPersistentDataContainer().get(key, type));
    }

    public static void setPersistent(ItemStack item, NamespacedKey key, String value) {
        setPersistent(item, key, value, PersistentDataType.STRING);
    }

    public static <T, Z> void setPersistent(ItemStack item, NamespacedKey key, Z value, PersistentDataType<T, Z> type) {
        item.editMeta(meta -> setPersistent(meta, key, value, type));
    }

    public static void setPersistent(ItemMeta meta, NamespacedKey key, String value) {
        setPersistent(meta, key, value, PersistentDataType.STRING);
    }

    public static <T, Z> void setPersistent(ItemMeta meta, NamespacedKey key, Z value, PersistentDataType<T, Z> type) {
        meta.getPersistentDataContainer().set(key, type, value);
    }

    // itemMeta is nullable
    public static boolean hasPersistent(ItemStack item, NamespacedKey key) {
        return item.getItemMeta() != null && hasPersistent(item.getItemMeta(), key);
    }

    public static boolean hasPersistent(ItemMeta meta, NamespacedKey key) {
        return meta.getPersistentDataContainer().has(key);
    }

    public static void setItem(Player player, int slot, ItemStack item) {
        player.getInventory().setItem(slot, item);
    }

    public static void setItem(Player player, EquipmentSlot equipmentSlot, ItemStack item) {
        player.getInventory().setItem(equipmentSlot, item);
    }

    public static synchronized void removeSlot(Player player, int slot) {
        player.getInventory().setItem(slot, AIR);
    }

    public static synchronized void removeSlot(Player player, EquipmentSlot equipmentSlot) {
        player.getInventory().setItem(equipmentSlot, AIR);
    }

    /**
     * 1. 일단 인벤토리에 저장 시도
     * <br>
     * 2. 안되면 뱉음
     */
    public static synchronized void giveItem(Player player, ItemStack item) {
        int emptySlot = player.getInventory().firstEmpty();
        if(emptySlot == -1) player.getWorld().dropItem(player.getLocation(), item);
        else setItem(player, emptySlot, item);
    }

    public static synchronized void addItem(Player player, ItemStack item) {
        if(!player.getInventory().addItem(item).isEmpty())
            player.getWorld().dropItem(player.getLocation(), item);
    }

    public static boolean isFull(Inventory inventory) {
        return inventory.getMaxStackSize() == inventory.getSize();
    }
}