package com.zemnitskiy.perfecttrip.planner;

import java.util.List;

public record Alternative(
        String polyline,
        List<Poi> pois,
        int etaMin,
        double distanceKm
) {
}
