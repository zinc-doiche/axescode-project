package com.github.axescode.core.shop;

import com.github.axescode.container.Data;
import lombok.*;
import org.bukkit.inventory.ItemStack;

@Getter @Setter @ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ShopItemData implements Data {
    private Long shopItemId;
    private String playerName;
    private ItemStack shopItem;
    private Long shopItemPrice;
    private Integer shopItemStock;

    @Override
    public Long key() {
        return shopItemId;
    }
}
