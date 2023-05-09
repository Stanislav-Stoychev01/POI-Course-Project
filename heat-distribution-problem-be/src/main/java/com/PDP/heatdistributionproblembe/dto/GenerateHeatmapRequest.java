package com.PDP.heatdistributionproblembe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GenerateHeatmapRequest {

    @Schema(description = "Mathematical function that will determine the value of the block. Must contain x and y params")
    String userFunction;

    @Schema(description = "Size of the heatmap", example = "10")
    Integer arraySize;

    @Schema(description = "Number of concurrent threads", example = "3")
    Integer numberOfWorkers;

    @Schema(description = "Number of steps made by each worker", example = "5")
    Integer stepsOfWorker;
}
