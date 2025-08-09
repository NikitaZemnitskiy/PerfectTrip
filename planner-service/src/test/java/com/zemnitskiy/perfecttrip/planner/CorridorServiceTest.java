package com.zemnitskiy.perfecttrip.planner;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;

import static org.junit.jupiter.api.Assertions.*;

public class CorridorServiceTest {

    private final GeometryFactory gf = new GeometryFactory();
    private final CorridorService service = new CorridorService();

    @Test
    void areaRoughlyMatchesBuffer() {
        LineString line = gf.createLineString(new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(0.009,0)
        });
        Geometry corridor = service.build(line, Mode.DRIVING_CAR);
        Geometry mercator = toMercator(corridor);
        double area = mercator.getArea();
        double r = 1000.0;
        double length = 1000.0;
        double expected = 2 * r * length + Math.PI * r * r;
        assertTrue(Math.abs(area - expected) / expected < 0.2); // within 20%
        assertTrue(corridor.isValid());
    }

    private Geometry toMercator(Geometry geom) {
        Geometry copy = geom.copy();
        copy.apply(new CoordinateSequenceFilter() {
            @Override
            public void filter(CoordinateSequence seq, int i) {
                Coordinate c = seq.getCoordinate(i);
                double x = Math.toRadians(c.x) * 6378137.0;
                double y = Math.log(Math.tan(Math.PI / 4 + Math.toRadians(c.y) / 2)) * 6378137.0;
                seq.setOrdinate(i, 0, x);
                seq.setOrdinate(i, 1, y);
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public boolean isGeometryChanged() {
                return true;
            }
        });
        return copy;
    }
}
