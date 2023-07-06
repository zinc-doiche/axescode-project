package com.github.axescode.core.job.none;

import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.JobAbility;
import com.github.axescode.core.job.JobTripod;
import lombok.RequiredArgsConstructor;


/**
 * {@link com.github.axescode.core.job.Job.Type}의 기본타입
 */
@RequiredArgsConstructor
public class JobImpl implements Job {
    private final JobTripod<JobImpl, JobSkillImpl> tripod;
    private final JobAbility<JobImpl> ability;

    @Override
    public JobAbilityImpl getAbility() {
        return (JobAbilityImpl) ability;
    }

    @Override
    public Type getJobType() {
        return Type.NONE;
    }

    @Override
    public JobTripodImpl getTripod() {
        return (JobTripodImpl) tripod;
    }
}
