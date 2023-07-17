package com.github.axescode.mybatis.mapper;

import com.github.axescode.core.player.PlayerVO;
import com.github.axescode.mybatis.IMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayerMapper extends IMapper {
    void insert(PlayerVO playerVO);
    PlayerVO select(Long playerId);
    PlayerVO selectByName(String playerName);
    void fastInsert(String playerName);
    void update(PlayerVO playerVO);
}
