package com.zemnitskiy.perfecttrip.planner;

import java.util.List;

public record Alternative(
        String route,
        String corridor,
        List<Poi> pois,
        int etaMin,
        double distanceKm
) {
}
