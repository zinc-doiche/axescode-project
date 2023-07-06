package com.github.axescode.front.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerEventAdapter extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerEventAdapter(@NotNull Player player) {
        super(player, false);
    }

    public PlayerEventAdapter(@NotNull Player player, boolean async) {
        super(player, async);
    }

    private HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers()  {
        return getHandlerList();
    }
}
