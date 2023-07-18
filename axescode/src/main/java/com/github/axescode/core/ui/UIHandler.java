package com.github.axescode.core.ui;

import com.github.axescode.core.ui.template.UI;
import org.bukkit.entity.Player;

/**
 * {@link UI}를 관리합니다.
 */
public interface UIHandler {
    //Viewer getViewer();

    /**
     * {@link Viewer}에게 {@link UI}을 보여줍니다.
     * @see UI#openUI(Player)
     */
    void openUI();
}
