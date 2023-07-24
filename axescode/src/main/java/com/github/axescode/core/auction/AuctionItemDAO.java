package com.github.axescode.core.auction;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.core.AbstractDAO;
import com.github.axescode.mybatis.mapper.AuctionItemMapper;
import com.github.axescode.util.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AuctionItemDAO extends AbstractDAO<AuctionItemDAO> implements Transactional {
    private final AuctionItemMapper mapper = sqlSession.getMapper(AuctionItemMapper.class);

    protected AuctionItemDAO() { super(true); }
    protected AuctionItemDAO(boolean autoCommit) { super(autoCommit); }

    @Override
    public void commit() {
        sqlSession.commit();
    }

    @Override
    public void rollback() {
        sqlSession.rollback();
    }

    public void save(AuctionItemVO auctionItemVO) {
        mapper.insert(auctionItemVO);
    }

    public List<AuctionItemVO> findAll(String keyword) {
        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);
        return mapper.selectAll(data);
    }

    public List<AuctionItemVO> findAll(Long playerId) {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", playerId);
        return mapper.selectAll(data);
    }

    public List<AuctionItemVO> findAllWithLimit(String keyword, int selectedPage, int sizePerPage) {
        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);
        data.put("start", ((selectedPage - 1) * sizePerPage + 1));
        data.put("end", (selectedPage * sizePerPage));
        return mapper.selectAll(data);
    }

    public List<AuctionItemVO> findAllWithLimit(Long playerId, int selectedPage, int sizePerPage) {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", playerId);
        data.put("start", ((selectedPage - 1) * sizePerPage + 1));
        data.put("end", (selectedPage * sizePerPage));
        return mapper.selectAll(data);
    }

    public List<AuctionItemVO> findAllWithLimit(Long playerId, String keyword, int selectedPage, int sizePerPage) {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", playerId);
        data.put("keyword", keyword);
        data.put("start", ((selectedPage - 1) * sizePerPage + 1));
        data.put("end", (selectedPage * sizePerPage));
        return mapper.selectAll(data);
    }

    public void modify(AuctionItemVO auctionItemVO) {
        mapper.modify(auctionItemVO);
    }

    public void remove(AuctionItemVO auctionItemVO) {
        mapper.remove(auctionItemVO);
    }

    public static void use(Consumer<AuctionItemDAO> consumer) {
        AuctionItemDAO dao = new AuctionItemDAO();
        consumer.accept(dao);
        dao.close();
    }

    public static void useTransaction(Consumer<AuctionItemDAO> consumer) {
        AuctionItemDAO dao = new AuctionItemDAO(true);
        try {
            consumer.accept(dao);
            dao.commit();
        } catch(Exception e) {
            try {
                AxescodePlugin.warn("[" + dao.getClass().getName() + "] 트랜잭션 중 실패하여 롤백합니다.");
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

    public static AuctionItemData toData(AuctionItemVO auctionItemVO) {
        return AuctionItemData.builder()
                .auctionItemId(auctionItemVO.getAuctionItemId())
                .build();
    }
}
