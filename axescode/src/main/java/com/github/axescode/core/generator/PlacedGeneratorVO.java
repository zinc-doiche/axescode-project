package com.github.axescode.core.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder
public class PlacedGeneratorVO {
    private Long placedGeneratorId;
    private String placedGeneratorName;
    private String placedGeneratorWorld;
    private Integer placedGeneratorX;
    private Integer placedGeneratorY;
    private Integer placedGeneratorZ;
}
