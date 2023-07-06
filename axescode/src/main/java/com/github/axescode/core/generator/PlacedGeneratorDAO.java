package com.github.axescode.core.generator;

import com.github.axescode.core.AbstractDAO;
import com.github.axescode.mybatis.mapper.PlacedGeneratorMapper;

import java.util.List;
import java.util.function.Consumer;

public class PlacedGeneratorDAO extends AbstractDAO<PlacedGeneratorDAO> {
    private final PlacedGeneratorMapper mapper = sqlSession.getMapper(PlacedGeneratorMapper.class);

    protected PlacedGeneratorDAO() {
        super(true);
    }

    public List<PlacedGeneratorVO> findAll() {
        return mapper.selectAll();
    }

    public void save(PlacedGeneratorVO placedGeneratorVO) {
        mapper.insert(placedGeneratorVO);
    }

    public void remove(Long placedGeneratorId) {
        mapper.delete(placedGeneratorId);
    }

    public static void use(Consumer<PlacedGeneratorDAO> consumer) {
        PlacedGeneratorDAO dao = new PlacedGeneratorDAO();
        consumer.accept(dao);
        dao.close();
    }
}
