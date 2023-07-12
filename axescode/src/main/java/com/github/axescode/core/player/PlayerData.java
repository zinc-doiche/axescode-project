package com.github.axescode.core.player;

import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.Proficiency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
@Builder
public class PlayerData implements com.github.axescode.container.Data {
    private Long playerId;
    private String playerName;
    private String playerNickName;
    private Long playerMoney;
    private Player playerEntity;
    private Job playerJob;
    private Proficiency<? extends Job> playerProficiency;
}
