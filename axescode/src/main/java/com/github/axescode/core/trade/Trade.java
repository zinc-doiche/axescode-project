package com.github.axescode.core.trade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trade {
    private Long tradeId;
    private Long accepterId;
    private Long requesterId;
    private String tradeDateTime;
}
