package com.github.axescode.core.ui;

import com.github.axescode.core.ui.template.UITemplate;
import org.bukkit.entity.Player;

/**
 * {@link UITemplate}를 관리합니다.
 */
public interface UIHandler {
    //Viewer getViewer();

    /**
     * {@link Viewer}에게 {@link UITemplate}을 보여줍니다.
     * @see UITemplate#openUI(Player) 
     */
    void openUI();
}
