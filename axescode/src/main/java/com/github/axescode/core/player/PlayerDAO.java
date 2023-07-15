package com.github.axescode.core.player;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.Jobs;
import com.github.axescode.core.job.Proficiency;
import com.github.axescode.mybatis.mapper.PlayerMapper;
import com.github.axescode.core.AbstractDAO;
import com.github.axescode.util.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

public class PlayerDAO extends AbstractDAO<PlayerDAO> implements Transactional {
    private final PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);

    protected PlayerDAO() {
        super(true);
    }

    protected PlayerDAO(boolean autoCommit) {
        super(autoCommit);
    }

    @Override
    public void commit() {
        sqlSession.commit();
    }

    @Override
    public void rollback() {
        sqlSession.rollback();
    }

    public void fastSave(String playerName) {
        mapper.fastInsert(playerName);
    }

    public void save(PlayerData playerData) {
        mapper.insert(toVO(playerData));
    }

    public PlayerData findPlayer(Long playerId) {
        return toData(mapper.select(playerId));
    }

    public PlayerData findPlayerByName(String playerName) {
        return toData(mapper.selectByName(playerName));
    }

    public void modify(PlayerVO playerVO) {
        mapper.update(playerVO);
    }

    public static void use(Consumer<PlayerDAO> consumer) {
        PlayerDAO dao = new PlayerDAO();
        consumer.accept(dao);
        dao.close();
    }

    public static void useTransaction(Consumer<PlayerDAO> consumer) {
        PlayerDAO dao = new PlayerDAO(true);
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

    public static PlayerVO toVO(PlayerData playerData) {
        return PlayerVO.builder()
                .playerId(playerData.getPlayerId())
                .playerName(playerData.getPlayerName())
                .playerNickName(playerData.getPlayerNickName())
                .playerJobType(playerData.getPlayerJob().getJobType().name())
                .playerMoney(playerData.getPlayerMoney())
                .playerProficiencyStack(playerData.getPlayerProficiency().getProficiencyStack())
                .playerProficiencyLevel(playerData.getPlayerProficiency().getProficiencyLevel())
                .build();
    }

    public static PlayerData toData(PlayerVO playerVO) {
        return PlayerData.builder()
                .playerId(playerVO.getPlayerId())
                .playerName(playerVO.getPlayerName())
                .playerNickName(playerVO.getPlayerNickName())
                .playerMoney(playerVO.getPlayerMoney())
                .playerJob(Jobs.get(Job.Type.valueOf(playerVO.getPlayerJobType())))
                .playerProficiency(
                        Proficiency.builder()
                                .job(Jobs.get(Job.Type.valueOf(playerVO.getPlayerJobType())))
                                .proficiencyLevel(playerVO.getPlayerProficiencyLevel())
                                .proficiencyStack(playerVO.getPlayerProficiencyStack())
                                .build()
                )
                .playerEntity(AxescodePlugin.inst().getServer().getPlayer(playerVO.getPlayerName()))
                .build();
    }
}
