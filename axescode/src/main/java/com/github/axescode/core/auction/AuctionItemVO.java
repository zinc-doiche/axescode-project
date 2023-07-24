package com.github.axescode.core.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Player Customized Shop Items
 */
@Data
@AllArgsConstructor
@Builder
public class AuctionItemVO {
    private Long auctionItemId;
    private Long playerId;
    private String auctionItemName;
    private String auctionItemOriginName;
    private String auctionItem;
    private Long auctionItemPrice;
    private Integer auctionItemStock;
}
