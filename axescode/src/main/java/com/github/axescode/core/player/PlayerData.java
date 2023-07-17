package com.github.axescode.core.player;

import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.Proficiency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.github.axescode.container.Data;
import org.bukkit.entity.Player;

@lombok.Data
@AllArgsConstructor
@Builder
public class PlayerData implements Data {
    private Long playerId;
    private String playerName;
    private String playerNickName;
    private Long playerMoney;
    private Player playerEntity;
    private Job playerJob;
    private Proficiency<? extends Job> playerProficiency;

    @Override
    public Long key() {
        return playerId;
    }
}
