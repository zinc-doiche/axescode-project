package com.github.axescode.core.player;

import com.github.axescode.AxescodePlugin;
import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.Jobs;
import com.github.axescode.core.job.Proficiency;
import com.github.axescode.mybatis.mapper.PlayerMapper;
import com.github.axescode.core.AbstractDAO;

import java.util.Optional;
import java.util.function.Consumer;

public class PlayerDAO extends AbstractDAO<PlayerDAO> {
    private final PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);

    protected PlayerDAO() {
        super(true);
    }

    public void fastSave(String playerName) {
        mapper.fastInsert(playerName);
    }

    public void save(PlayerData playerData) {
        mapper.insert(toVO(playerData));
    }

    public Optional<PlayerData> findPlayer(Long playerId) {
        return Optional.ofNullable(mapper.select(playerId)).map(PlayerDAO::toData);
    }

    public Optional<PlayerData> findPlayerByName(String playerName) {
        return Optional.ofNullable(mapper.selectByName(playerName)).map(PlayerDAO::toData);
    }

    public static void use(Consumer<PlayerDAO> consumer) {
        PlayerDAO dao = new PlayerDAO();
        consumer.accept(dao);
        dao.close();
    }

    public static PlayerVO toVO(PlayerData playerData) {
        return PlayerVO.builder()
                .playerId(playerData.getPlayerId())
                .playerName(playerData.getPlayerName())
                .playerNickName(playerData.getPlayerNickName())
                .playerJobType(playerData.getPlayerJob().getJobType().name())
                .playerProficiencyStack(playerData.getPlayerProficiency().getProficiencyStack())
                .playerProficiencyLevel(playerData.getPlayerProficiency().getProficiencyLevel())
                .build();
    }

    public static PlayerData toData(PlayerVO playerVO) {
        return PlayerData.builder()
                .playerId(playerVO.getPlayerId())
                .playerName(playerVO.getPlayerName())
                .playerNickName(playerVO.getPlayerNickName())
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
