package com.github.axescode.core;

import com.github.axescode.AbstractTest;
import com.github.axescode.core.auction.AuctionItemDAO;
import com.github.axescode.core.auction.AuctionItemVO;
import com.github.axescode.core.player.PlayerDAO;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;

public class AuctionItemTests extends AbstractTest {

    @Test
    public void saveTest() {
        AuctionItemDAO.useTransaction(dao -> {
            final Long id = (Long) PlayerDAO.useReturn(playerDAO ->
                    playerDAO.findPlayerByName("LE_CONTRAIL").getPlayerId());

            AuctionItemVO item = AuctionItemVO.builder()
                    .playerId(id)
                    .auctionItemName("루비 곡괭이 +1강")
                    .auctionItemOriginName("루비 곡괭이")
                    .auctionItem("test item")
                    .auctionItemStock(32)
                    .auctionItemPrice(100_000L)
                    .build();
            dao.save(item);
        });
    }

    @Test
    public void findTest() {
        AuctionItemDAO.use(dao -> {
            List<AuctionItemVO> list =  dao.findAll(1L);
            info(list);
        });
    }
}
