package com.github.axescode.core.ui;

import com.github.axescode.core.ui.template.UITemplate;
import org.bukkit.entity.Player;

/**
 * {@link UITemplate}를 보고 있는 플레이어를 나타냅니다.
 */
public interface Viewer {
    //Player getPlayer();
    UIHandler getHandler();

    /**
     * <h2>!!re-open 시에만 사용할 것.</h2>
     * @see UIHandler#openUI()
     */
    void openUI();
    void closeUI();
}
