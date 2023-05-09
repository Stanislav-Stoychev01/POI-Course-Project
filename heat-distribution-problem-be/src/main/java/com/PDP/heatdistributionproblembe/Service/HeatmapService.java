package com.PDP.heatdistributionproblembe.Service;

import com.PDP.heatdistributionproblembe.Threads.Walker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
@Data
@RequiredArgsConstructor
public class HeatmapService {

    private ReentrantLock reentrantReadLock = new ReentrantLock();

    private ReentrantLock reentrantWriteLock = new ReentrantLock();

    public Double[][] generateHeatmap(Integer heatmapLength, String input, Integer numberOfWorkers, Integer numberOfSteps) {

        Double[][] heatmap = new Double[heatmapLength][heatmapLength];

        for(int i = 0; i < numberOfWorkers; i++) {
            Walker walker = new Walker(reentrantReadLock, reentrantWriteLock, heatmap, input, numberOfSteps);
            walker.run();
        }

        return heatmap;
    }

}