package com.github.axescode.core.job.none;

import com.github.axescode.core.job.JobTripod;

public record JobTripodImpl(JobSkillImpl[] part1, JobSkillImpl[] part2, JobSkillImpl[] part3) implements JobTripod<JobImpl, JobSkillImpl> {
    public JobTripodImpl {
        assert part1.length == PART_1_SKILL_NUMBER;
        assert part2.length == PART_2_SKILL_NUMBER;
        assert part3.length == PART_3_SKILL_NUMBER;
    }
}
