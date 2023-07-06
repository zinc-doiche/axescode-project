package com.github.axescode.core.job;

import lombok.Getter;

/**
 * AXESCODE의 직업 시스템을 나타냅니다.
 *
 *
 */
public interface Job {

    /**
     *
     * @return 해당 직업의 {@link JobAbility}
     */
    JobAbility<? extends Job> getAbility();

    /**
     *
     * @return 해당 직업의 {@link Type}
     */
    Type getJobType();

    /**
     *
     * @return 해당 직업의 {@link JobTripod}
     */
    JobTripod<? extends Job, ? extends JobSkill<? extends Job>> getTripod();

    /**
     * {@link Job}의 종류를 나타냅니다.
     */
    @Getter
    enum Type {
        NONE("없음"),
        FARMER("농부"),
        MINER("광부"),
        FISHER("어부");

        private final String kor;

        Type(String kor) {
            this.kor = kor;
        }
    }
}
