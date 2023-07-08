package com.github.axescode.core.trade;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.core.AbstractDAO;
import com.github.axescode.mybatis.mapper.TradeMapper;
import com.github.axescode.util.Transactional;

import java.util.List;
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

    public List<TradeItemVO> findAllById(Long tradeId) {
        return mapper.selectAll(tradeId);
    }

    public static void use(Consumer<TradeDAO> consumer) {
        TradeDAO dao = new TradeDAO();
        try {
            consumer.accept(dao);
            dao.commit();
        } catch(Exception e) {
            try {
                AxescodePlugin.warn("트랜잭션 중 실패하여 롤백합니다.");
                e.printStackTrace();
                dao.rollback();
            } catch (Exception ex) {
                AxescodePlugin.warn("롤백에 실패하였습니다. 즉시 데이터베이스를 점검하세요.");
                e.printStackTrace();
            }
        } finally {
            dao.close();
        }
    }
}
