package com.github.axescode.core.job.none;

import com.github.axescode.core.job.JobSkill;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobSkillImpl implements JobSkill<JobImpl> {
    private final String skillName;

    @Override
    public String getSkillName() {
        return skillName;
    }
}
