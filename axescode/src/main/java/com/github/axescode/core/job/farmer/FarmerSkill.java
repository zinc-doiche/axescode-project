package com.github.axescode.core.job.farmer;

import com.github.axescode.core.job.JobSkill;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FarmerSkill implements JobSkill<Farmer> {
    private final String skillName;

    @Override
    public String getSkillName() {
        return skillName;
    }
}
