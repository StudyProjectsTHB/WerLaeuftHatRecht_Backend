package com.example.mux.challenge.model;

public enum ChallengeTypeEnum {
    /**
     * x = time
     * y = amount
     * CONSECUTIVELY: Laufe an x aufeinanderfolgenden Tagen y Schritte.
     * EACH_DAY: Laufe an x Tagen je y Schritte.
     * TOTAL_DAYS: Laufe an x Tagen insgesamt y Schritte.
     * TOTAL_STEPS: Laufe in dieser Woche insgesamt y Schritte.
     */
    CONSECUTIVELY, EACH_DAY, TOTAL_DAYS, TOTAL_STEPS
}
