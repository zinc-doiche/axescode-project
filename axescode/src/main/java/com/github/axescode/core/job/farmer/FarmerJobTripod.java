package com.github.axescode.core.job.farmer;

import com.github.axescode.core.job.JobTripod;
import com.github.axescode.core.job.farmer.Farmer;
import com.github.axescode.core.job.farmer.FarmerSkill;

/**
 * {@link Farmer}의 {@link JobTripod}를 나타냅니다.
 */
public record FarmerJobTripod(FarmerSkill[] part1, FarmerSkill[] part2, FarmerSkill[] part3) implements JobTripod<Farmer, FarmerSkill> {
    public FarmerJobTripod {
        assert part1.length == PART_1_SKILL_NUMBER;
        assert part2.length == PART_2_SKILL_NUMBER;
        assert part3.length == PART_3_SKILL_NUMBER;
    }
}
