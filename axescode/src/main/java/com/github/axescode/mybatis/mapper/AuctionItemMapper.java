package com.github.axescode.mybatis.mapper;

import com.github.axescode.core.auction.AuctionItemVO;
import com.github.axescode.mybatis.IMapper;

import java.util.List;
import java.util.Map;

public interface AuctionItemMapper extends IMapper {
    void insert(AuctionItemVO auctionItemVO);
    List<AuctionItemVO> selectAll(Map<String, Object> data);
    void modify(AuctionItemVO auctionItemVO);
    void remove(AuctionItemVO auctionItemVO);
}
