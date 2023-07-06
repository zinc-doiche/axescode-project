package com.github.axescode.mybatis.mapper;

import com.github.axescode.core.generator.PlacedGeneratorVO;
import com.github.axescode.mybatis.IMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlacedGeneratorMapper extends IMapper {
    List<PlacedGeneratorVO> selectAll();
    void insert(PlacedGeneratorVO placedGeneratorVO);
    void delete(Long placedGeneratorId);
}
