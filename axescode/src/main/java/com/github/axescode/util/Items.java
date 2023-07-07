package com.github.axescode.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class Items {
    public static final ItemStack AIR = new ItemStack(Material.AIR);

    public static @NotNull ItemStack item(Material material, Consumer<ItemMeta> block) {
        ItemStack item = new ItemStack(material);
        item.editMeta(block);
        return item;
    }

    public static @NotNull ItemStack item(Material material, Component name, Consumer<ItemMeta> block) {
        return item(material, meta -> {
            meta.displayName(name);
            block.accept(meta);
        });
    }

    public static @NotNull ItemStack getCustomItem(Material material, Component name, int customModelNumber, Consumer<ItemMeta> init) {
        return item(material, name, meta -> {
            meta.setCustomModelData(customModelNumber);
            init.accept(meta);
        });
    }

    public static @NotNull Boolean isNullOrAir(@Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static Optional<String> getPersistent(@NotNull ItemStack item, NamespacedKey key) {
        return getPersistent(item.getItemMeta(), key);
    }

    public static <T, Z> Optional<Z> getPersistent(@NotNull ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type) {
        return getPersistent(item.getItemMeta(), key, type);
    }

    public static Optional<String> getPersistent(@NotNull ItemMeta meta, NamespacedKey key) {
        return Optional.ofNullable(meta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
    }

    public static <T, Z> Optional<Z> getPersistent(@NotNull ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type) {
        return Optional.ofNullable(meta.getPersistentDataContainer().get(key, type));
    }

    public static void setPersistent(ItemStack item, NamespacedKey key, String value) {
        setPersistent(item, key, value, PersistentDataType.STRING);
    }

    public static <T, Z> void setPersistent(@NotNull ItemStack item, NamespacedKey key, Z value, PersistentDataType<T, Z> type) {
        item.editMeta(meta -> setPersistent(meta, key, value, type));
    }

    public static void setPersistent(ItemMeta meta, NamespacedKey key, String value) {
        setPersistent(meta, key, value, PersistentDataType.STRING);
    }

    public static <T, Z> void setPersistent(@NotNull ItemMeta meta, NamespacedKey key, Z value, PersistentDataType<T, Z> type) {
        meta.getPersistentDataContainer().set(key, type, value);
    }

    // itemMeta is nullable
    public static boolean hasPersistent(@NotNull ItemStack item, NamespacedKey key) {
        return item.getItemMeta() != null && hasPersistent(item.getItemMeta(), key);
    }

    public static boolean hasPersistent(@NotNull ItemMeta meta, NamespacedKey key) {
        return meta.getPersistentDataContainer().has(key);
    }

    public static void setItem(@NotNull Player player, int slot, ItemStack item) {
        player.getInventory().setItem(slot, item);
    }

    public static void setItem(@NotNull Player player, EquipmentSlot equipmentSlot, ItemStack item) {
        player.getInventory().setItem(equipmentSlot, item);
    }

    public static synchronized void removeSlot(@NotNull Player player, int slot) {
        player.getInventory().setItem(slot, AIR);
    }

    public static synchronized void removeSlot(@NotNull Player player, EquipmentSlot equipmentSlot) {
        player.getInventory().setItem(equipmentSlot, AIR);
    }

    /**
     * 해당 {@link Player}에게 아이템을 전달합니다. 마인크래프트의 기본적인 아이템 획득 방식과 동일하게 작동합니다.
     * 인벤토리 용량을 초과한 아이템은 그 자리에 떨어지게 됩니다.
     *
     * @param player 대상 {@link Player}
     * @param items 전달할 {@link ItemStack}
     */
    public static synchronized void addItem(@NotNull Player player, @NotNull ItemStack... items) {
        if(!player.getInventory().addItem(items).isEmpty()) {
            Arrays.stream(items).forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
        }
    }
}