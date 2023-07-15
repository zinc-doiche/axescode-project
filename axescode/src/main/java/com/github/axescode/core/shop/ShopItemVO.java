package com.github.axescode.core.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Player Customized Shop Items
 */
@Data
@AllArgsConstructor
@Builder
public class ShopItemVO {
    private Long shopItemId;
    private Long playerId;
    private String shopItem;
    private Long shopItemPrice;
    private Integer shopItemStock;
}
