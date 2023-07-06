package com.github.axescode.core.job;

import org.bukkit.event.Listener;

/**
 * 각 {@link Job}의 능력을 나타냅니다.
 *
 * @param <J> 해당 능력을 가지는 직업
 */
public interface JobAbility<J extends Job> {
    default void onClick() {;}
}
