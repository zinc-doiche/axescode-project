package com.github.axescode.mybatis.mapper;

import com.github.axescode.core.trade.TradeItemVO;
import com.github.axescode.core.trade.TradeVO;
import com.github.axescode.mybatis.IMapper;

import java.util.List;

public interface TradeMapper extends IMapper {
    void insert(TradeVO tradeVO);
    void insertItem(TradeItemVO tradeItemVO);

    List<TradeItemVO> selectAll(Long tradeId);
}
