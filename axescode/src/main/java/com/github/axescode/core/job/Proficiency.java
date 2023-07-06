package com.github.axescode.core.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proficiency<J extends Job> {
    private J job;
    private Integer proficiencyStack;
    private Integer proficiencyLevel;

    /**
     *
     * @param amount 더해줄 양
     * @return 더한 후 총 스택 수
     */
    public Integer addProficiencyStack(int amount) {
        proficiencyStack += amount;
        return proficiencyStack;
    }

    /**
     *
     * @param amount 더해줄 양
     * @return 더한 후 총 스택 수
     */
    public Integer addProficiencyLevel(int amount) {
        proficiencyLevel += amount;
        return proficiencyLevel;
    }
}
