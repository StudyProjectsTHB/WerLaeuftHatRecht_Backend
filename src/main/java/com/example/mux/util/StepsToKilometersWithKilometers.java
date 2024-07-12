package com.example.mux.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class StepsToKilometersWithKilometers extends  StepsToKilometers{
    private double kilometers;

    protected void calculateAndSetKilometers(int steps, Integer height, Integer stepSize){
        setKilometers(calculateKilometers(steps, height, stepSize));
    }
}
