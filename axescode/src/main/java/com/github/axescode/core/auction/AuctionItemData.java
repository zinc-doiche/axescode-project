package com.github.axescode.core.auction;

import com.github.axescode.container.Data;
import lombok.*;
import org.bukkit.inventory.ItemStack;

@Getter @Setter @ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionItemData implements Data {
    private Long auctionItemId;
    private String playerName;
    private ItemStack shopItem;
    private Long auctionItemPrice;
    private Integer auctionItemStock;

    @Override
    public Long key() {
        return auctionItemId;
    }
}
