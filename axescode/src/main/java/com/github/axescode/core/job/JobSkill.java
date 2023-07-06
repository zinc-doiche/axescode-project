package com.github.axescode.core.job;

/**
 * {@link JobTripod}의 스킬을 나타냅니다.
 */
public interface JobSkill<J extends Job> {

    /**
     *
     * @return 헤당 스킬의 이름
     */
    String getSkillName();
}
