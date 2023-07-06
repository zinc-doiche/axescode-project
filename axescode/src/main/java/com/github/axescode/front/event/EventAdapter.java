package com.github.axescode.front.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class EventAdapter extends Event {
    private static final HandlerList handlers = new HandlerList();

    public EventAdapter(boolean async) {
        super(async);
    }

    private HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers()  {
        return getHandlerList();
    }
}
