package com.PDP.heatdistributionproblembe.Controller;

import com.PDP.heatdistributionproblembe.Service.HeatmapService;
import com.PDP.heatdistributionproblembe.dto.GenerateHeatmapRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mariuszgromada.math.mxparser.License;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Heatmap")
public class HeatmapController {

    private final HeatmapService heatmapService;

    @PostMapping ("/generate-heatmap")
    public Double[][] getHeatmap(@RequestBody GenerateHeatmapRequest generateHeatmapRequest) {
        License.iConfirmNonCommercialUse("Stanislav Stoychev");

        return heatmapService.generateHeatmap(
                generateHeatmapRequest.getArraySize(),
                generateHeatmapRequest.getUserFunction(),
                generateHeatmapRequest.getNumberOfWorkers(),
                generateHeatmapRequest.getStepsOfWorker()
        );
    }
}
