package com.DipanshuChaudhary.project.uber.UberApplication.strategies;


import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.DriverMatchingNearestDriverMatchingStrategyImpl;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.RideFareDefaultFareCalculationStrategyImpl;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {


    // TODO   dynamic strategy    -----> how strategy pattern works


    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverMatchingStrategyImpl nearestDriverMatchingStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RideFareDefaultFareCalculationStrategyImpl defaultFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){
        if (riderRating <= 4.8){
            return highestRatedDriverStrategy;
        }
        else{
            return nearestDriverMatchingStrategy;
        }
    }


    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        // 6PM to 9PM
        LocalTime surgeStartTime = LocalTime.of(18,0);
        LocalTime surgeEndTime = LocalTime.of(21,0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        }
        else{
            return defaultFareCalculationStrategy;
        }
    }

}
