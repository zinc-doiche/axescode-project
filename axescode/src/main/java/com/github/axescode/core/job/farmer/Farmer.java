package com.github.axescode.core.job.farmer;

import com.github.axescode.core.job.Job;
import com.github.axescode.core.job.JobAbility;
import com.github.axescode.core.job.JobTripod;
import lombok.RequiredArgsConstructor;

/**
 * AXESCODE의 농부를 나타냅니다.
 */
@RequiredArgsConstructor
public class Farmer implements Job {
    private final JobTripod<Farmer, FarmerSkill> farmerTripod;
    private final JobAbility<Farmer> farmerAbility;

    @Override
    public FarmerAbility getAbility() {
        return (FarmerAbility) farmerAbility;
    }

    @Override
    public Type getJobType() {
        return Type.FARMER;
    }

    @Override
    public FarmerJobTripod getTripod() {
        return (FarmerJobTripod) farmerTripod;
    }
}
