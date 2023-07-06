package com.github.axescode.core.job;

import com.github.axescode.core.job.farmer.Farmer;
import com.github.axescode.core.job.farmer.FarmerAbility;
import com.github.axescode.core.job.farmer.FarmerJobTripod;
import com.github.axescode.core.job.farmer.FarmerSkill;
import com.github.axescode.core.job.none.JobAbilityImpl;
import com.github.axescode.core.job.none.JobImpl;
import com.github.axescode.core.job.none.JobSkillImpl;
import com.github.axescode.core.job.none.JobTripodImpl;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Job}을 관리/보관합니다.
 */
@Data
public class Jobs {
    private static final JobTripod<Farmer, FarmerSkill> farmerTripod;
    private static final JobTripod<JobImpl, JobSkillImpl> jobTripod;
    private static final Job farmer;
    private static final Job job;

    private static final Map<Job.Type, Job> jobs = new HashMap<>();

    public static @Nullable Job get(Job.Type jobType) {
        if(jobs.containsKey(jobType))
            return jobs.get(jobType);
        return null;
    }

    static {
        jobTripod = new JobTripodImpl(
                new JobSkillImpl[]{new JobSkillImpl("1-1"), new JobSkillImpl("1-2"), new JobSkillImpl("1-3")},
                new JobSkillImpl[]{new JobSkillImpl("2-1"), new JobSkillImpl("2-2"), new JobSkillImpl("2-3")},
                new JobSkillImpl[]{new JobSkillImpl("3-1"), new JobSkillImpl("3-2")}
        );
        farmerTripod = new FarmerJobTripod(
                new FarmerSkill[]{new FarmerSkill("1-1"), new FarmerSkill("1-2"), new FarmerSkill("1-3")},
                new FarmerSkill[]{new FarmerSkill("2-1"), new FarmerSkill("2-2"), new FarmerSkill("2-3")},
                new FarmerSkill[]{new FarmerSkill("3-1"), new FarmerSkill("3-2")}
        );

        farmer = new Farmer(farmerTripod, new FarmerAbility());
        job = new JobImpl(jobTripod, new JobAbilityImpl());

        jobs.put(Job.Type.NONE, job);
        jobs.put(Job.Type.FARMER, farmer);
    }

}
