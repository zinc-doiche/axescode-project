package com.github.axescode.core.shop;

import com.destroystokyo.paper.event.server.GS4QueryEvent;
import com.github.axescode.AxescodePlugin;
import com.github.axescode.container.Containers;
import com.github.axescode.core.AbstractDAO;
import com.github.axescode.mybatis.mapper.ShopItemMapper;
import com.github.axescode.util.Transactional;

import java.util.List;
import java.util.function.Consumer;

public class ShopItemDAO extends AbstractDAO<ShopItemDAO> implements Transactional {
    private final ShopItemMapper mapper = sqlSession.getMapper(ShopItemMapper.class);

    protected ShopItemDAO() { super(true); }
    protected ShopItemDAO(boolean autoCommit) { super(autoCommit); }

    @Override
    public void commit() {
        sqlSession.commit();
    }

    @Override
    public void rollback() {
        sqlSession.rollback();
    }

    public void save(ShopItemVO shopItemVO) {

    }

    public List<ShopItemVO> findAll(String keyword) {
        return null;
    }

    public List<ShopItemVO> findAll(Long playerId) {
        return null;
    }

    public List<ShopItemVO> findAllWithLimit(String keyword, int selectedPage, int sizePerPage) {
        return null;
    }

    public List<ShopItemVO> findAllWithLimit(Long playerId, int selectedPage, int sizePerPage) {
        return null;
    }

    public List<ShopItemVO> findAllWithLimit(Long playerId, String keyword, int selectedPage, int sizePerPage) {
        return null;
    }

    public void modify(ShopItemVO shopItemVO) {

    }

    public void remove(ShopItemVO shopItemVO) {

    }

    public static void use(Consumer<ShopItemDAO> consumer) {
        ShopItemDAO dao = new ShopItemDAO();
        consumer.accept(dao);
        dao.close();
    }

    public static void useTransaction(Consumer<ShopItemDAO> consumer) {
        ShopItemDAO dao = new ShopItemDAO(true);
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

    public static ShopItemData toData(ShopItemVO shopItemVO) {
        return ShopItemData.builder()
                .shopItemId(shopItemVO.getShopItemId())
                .shopItemId(shopItemVO.getShopItemId())
                .build();
    }
}
