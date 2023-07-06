package com.github.axescode.core.job.farmer;

import com.github.axescode.core.job.JobAbility;
import com.github.axescode.core.player.PlayerData;
import com.github.axescode.util.RollRandom;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * {@link Farmer}의 능력을 나타냅니다.
 * <br/>
 * <br/> - 성장한 농작물 수확 시 0.15% 확률로 숙련도 1 (default) 을 얻습니다.
 * <br/>
 * <br/> ※ 수박과 호박 그리고 사탕수수 같은 블럭형 농작물은 자란 농작물만 숙련도를 얻습니다.
 * <br/> ( 설치한 농작물, 성장 중인 농작물은 숙련도 지급 X )
 * <br/>
 * <br/> ※ 작물 리스트
 * <br/>    └ Wheat(밀) Carrot(당근) Potato(감자) Pumpkin(호박) Melon(수박) Sugar Cane(사탕수수) Beetroot(비트) Nether Wart(네더 와트) Cocoa Beans(코코아 콩)
 */
public class FarmerAbility implements JobAbility<Farmer> {
    public void onCrop(BlockBreakEvent e, PlayerData playerData) {
        if(RollRandom.roll(0.15)) {
            playerData.getPlayerProficiency().addProficiencyStack(1);
            playerData.getPlayerEntity().sendMessage("숙련도 증가! \n현재 숙련도: " + playerData.getPlayerProficiency().getProficiencyStack());
        }
    }


}