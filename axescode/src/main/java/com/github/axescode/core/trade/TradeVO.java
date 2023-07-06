package com.github.axescode.core.trade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradeVO {
    private Long tradeId;
    private Long tradeAcceptorId;
    private Long tradeRequesterId;
    private Long tradeAcceptorSentMoney;
    private Long tradeRequesterSentMoney;
    private String tradeDateTime;
}
