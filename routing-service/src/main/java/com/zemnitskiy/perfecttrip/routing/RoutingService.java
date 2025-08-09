package com.zemnitskiy.perfecttrip.routing;

public interface RoutingService {

    Route route(double startLat, double startLon,
                double endLat, double endLon,
                String profile);
}
