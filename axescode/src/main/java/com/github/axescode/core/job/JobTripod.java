package com.github.axescode.core.job;

/**
 * {@link Job}의 스킬 트리
 *
 * @param <J> 해당 {@link JobTripod}를 가지는 {@link Job}
 */
public interface JobTripod<J extends Job, S extends JobSkill<J>> {
    int PART_1_SKILL_NUMBER = 3;
    int PART_2_SKILL_NUMBER = 3;
    int PART_3_SKILL_NUMBER = 2;

    /**
     * 해당 직업의 1차 공용 스킬 3개를 가져옵니다.
     *
     * @return part 1의 스킬 3개
     */
    S[] part1();

    /**
     * 해당 직업의 2차 전용 스킬 3개를 가져옵니다.
     *
     * @return part 2의 스킬 3개
     */
    S[] part2();

    /**
     * 해당 직업의 3차 전용 스킬 2개를 가져옵니다.
     *
     * @return part 3의 스킬 2개
     */
    S[] part3();
}
