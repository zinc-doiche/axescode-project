package com.github.axescode.container;

import com.github.axescode.core.player.PlayerData;

public class Containers {
    private static final Container<PlayerData> playerDataContainer;

    static {
        playerDataContainer = new Container<>();
    }

    public static Container<PlayerData> getPlayerDataContainer() {
        return playerDataContainer;
    }
}
