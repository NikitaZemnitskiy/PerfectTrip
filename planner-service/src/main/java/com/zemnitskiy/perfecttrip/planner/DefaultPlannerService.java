package com.zemnitskiy.perfecttrip.planner;

import com.zemnitskiy.perfecttrip.routing.Route;
import com.zemnitskiy.perfecttrip.routing.RoutingService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPlannerService implements PlannerService {
    private final RoutingService routingService;
    private final CorridorService corridorService;

    public DefaultPlannerService(RoutingService routingService, CorridorService corridorService) {
        this.routingService = routingService;
        this.corridorService = corridorService;
    }

    @Override
    public PlanResponse plan(PlanRequest request) {
        Route route = routingService.route(
                request.start().lat(), request.start().lon(),
                request.end().lat(), request.end().lon(),
                request.mode().profile());
        Geometry corridor = corridorService.build(route.geometry(), request.mode());
        GeoJsonWriter writer = new GeoJsonWriter();
        Alternative alt = new Alternative(
                writer.write(route.geometry()),
                writer.write(corridor),
                List.of(),
                (int) Math.round(route.durationSeconds() / 60.0),
                route.distanceMeters() / 1000.0
        );
        return new PlanResponse(List.of(alt));
    }
}
