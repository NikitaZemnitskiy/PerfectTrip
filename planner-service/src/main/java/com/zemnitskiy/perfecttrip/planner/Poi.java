package com.zemnitskiy.perfecttrip.planner;

import java.util.List;

public record Poi(
        String id,
        String name,
        double lat,
        double lon,
        List<String> categories,
        Double rating,
        Integer timeToVisitMin,
        String source
) {
}
