package com.zemnitskiy.perfecttrip.routing;

import org.locationtech.jts.geom.LineString;

public record Route(LineString geometry, double distanceMeters, double durationSeconds) {
}
