package com.github.axescode.core.trade;

import com.github.axescode.core.AbstractDAO;
import com.github.axescode.mybatis.mapper.TradeMapper;
import com.github.axescode.util.Transactional;

import java.util.function.Consumer;

public class TradeDAO extends AbstractDAO<TradeDAO> implements Transactional {
    private final TradeMapper mapper = sqlSession.getMapper(TradeMapper.class);

    @Override
    public void commit() {
        sqlSession.commit();
    }

    @Override
    public void rollback() {
        sqlSession.rollback();
    }

    //트랜잭션 필요
    protected TradeDAO() {super(false);}

    public void save(TradeVO tradeVO) {
        mapper.insert(tradeVO);
    }

    public void saveItem(TradeItemVO tradeItemVO) {
        mapper.insertItem(tradeItemVO);
    }

    public static void use(Consumer<TradeDAO> consumer) {
        TradeDAO dao = new TradeDAO();
        consumer.accept(dao);
        dao.close();
    }
}
