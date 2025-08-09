package com.zemnitskiy.perfecttrip.planner;

public enum Mode {
    DRIVING_CAR("driving-car"),
    FOOT_WALKING("foot-walking");

    private final String profile;

    Mode(String profile) {
        this.profile = profile;
    }

    public String profile() {
        return profile;
    }
}
