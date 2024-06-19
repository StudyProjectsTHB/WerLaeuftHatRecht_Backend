package com.example.mux.challenge.model;

import java.util.List;
import java.util.Random;

public enum ChallengeTypeEnum {
    /**
     * x = time
     * y = amount
     * CONSECUTIVELY: Laufe an x aufeinanderfolgenden Tagen y Schritte.
     * EACH_DAY: Laufe an x Tagen je y Schritte.
     * TOTAL_DAYS: Laufe an x Tagen insgesamt y Schritte.
     * TOTAL_STEPS: Laufe in dieser Woche insgesamt y Schritte.
     */
    CONSECUTIVELY, EACH_DAY, TOTAL_DAYS, TOTAL_STEPS;

    private static final List<ChallengeTypeEnum> VALUES =List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ChallengeTypeEnum randomType()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
