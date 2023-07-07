package com.github.axescode.core.trade;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeItemVO {
    private Long tradeItemId;
    private Long tradeId;
    private Long playerId;
    //JSON
    private String tradeItem;
}
