package com.example.mux.util;

public abstract class StepsToKilometers {
    private static final int AVERAGE_STEP_SIZE = 70;

    protected double calculateKilometers(int steps, Integer height, Integer stepSize){
        if(height == null && stepSize == null){
            return 0;
        }else if(height != null){
            if(height == 0){
                return (steps * AVERAGE_STEP_SIZE) / 100000.;
            }else {
                return (steps * height * 0.414) / 100000.;
            }
        }else{
            if(stepSize == 0){
                return (steps * AVERAGE_STEP_SIZE) / 100000.;
            }else {
                return (steps * stepSize) / 100000.;
            }
        }
    }
}
