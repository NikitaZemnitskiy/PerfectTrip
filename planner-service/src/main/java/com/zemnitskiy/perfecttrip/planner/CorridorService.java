package com.zemnitskiy.perfecttrip.planner;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.precision.GeometryPrecisionReducer;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CorridorService {

    private static final GeometryFactory GF = new GeometryFactory();
    private static final double ORS_EARTH_RADIUS = 6378137.0;

    public Geometry build(LineString line, Mode mode) {
        double bufferMeters = mode == Mode.DRIVING_CAR ? 1000 : 400;
        LineString mercator = toMercator(line);
        Geometry buffered = mercator.buffer(bufferMeters);
        Geometry simplified = DouglasPeuckerSimplifier.simplify(buffered, 10.0);
        Geometry latLon = toLatLon((Polygon) simplified);
        GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(new PrecisionModel(1e6));
        return reducer.reduce(latLon);
    }

    private LineString toMercator(LineString line) {
        Coordinate[] coords = Arrays.stream(line.getCoordinates())
                .map(this::toMercator)
                .toArray(Coordinate[]::new);
        return GF.createLineString(coords);
    }

    private Polygon toLatLon(Polygon poly) {
        LinearRing shell = toLatLon((LineString) poly.getExteriorRing());
        LinearRing[] holes = new LinearRing[poly.getNumInteriorRing()];
        for (int i = 0; i < holes.length; i++) {
            holes[i] = toLatLon((LineString) poly.getInteriorRingN(i));
        }
        return GF.createPolygon(shell, holes);
    }

    private LinearRing toLatLon(LineString ring) {
        Coordinate[] coords = Arrays.stream(ring.getCoordinates())
                .map(this::toLatLon)
                .toArray(Coordinate[]::new);
        return GF.createLinearRing(coords);
    }

    private Coordinate toMercator(Coordinate c) {
        double x = Math.toRadians(c.x) * ORS_EARTH_RADIUS;
        double y = Math.log(Math.tan(Math.PI / 4 + Math.toRadians(c.y) / 2)) * ORS_EARTH_RADIUS;
        return new Coordinate(x, y);
    }

    private Coordinate toLatLon(Coordinate c) {
        double lon = Math.toDegrees(c.x / ORS_EARTH_RADIUS);
        double lat = Math.toDegrees(2 * Math.atan(Math.exp(c.y / ORS_EARTH_RADIUS)) - Math.PI / 2);
        return new Coordinate(lon, lat);
    }
}
