package com.example.mux.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class StepsToKilometers {
    private double kilometers;
    private static final int AVERAGE_STEP_SIZE = 70;

    protected void calculateKilometers(int steps, Integer height, Integer stepSize){
        if(height == null && stepSize == null){
            setKilometers(0);
        }else if(height != null){
            if(height == 0){
                setKilometers((steps * AVERAGE_STEP_SIZE) / 100000.);
            }else {
                setKilometers((steps * height * 0.414) / 100000.);
            }
        }else{
            if(stepSize == 0){
                setKilometers((steps * AVERAGE_STEP_SIZE) / 100000.);
            }else {
                setKilometers((steps * stepSize) / 100000.);
            }
        }
    }
}
