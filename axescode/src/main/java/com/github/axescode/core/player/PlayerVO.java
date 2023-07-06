package com.github.axescode.core.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class PlayerVO {
    private Long playerId;
    private String playerName;
    private String playerNickName;
    private String playerJobType;
    private Integer playerProficiencyStack;
    private Integer playerProficiencyLevel;
}
