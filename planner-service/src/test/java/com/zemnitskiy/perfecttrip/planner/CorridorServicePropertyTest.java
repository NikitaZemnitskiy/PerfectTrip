package com.zemnitskiy.perfecttrip.planner;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import static org.junit.jupiter.api.Assertions.*;

public class CorridorServicePropertyTest {

    private final GeometryFactory gf = new GeometryFactory();
    private final CorridorService service = new CorridorService();

    @Property
    void bufferProducesValidPolygon(@ForAll double lat1, @ForAll double lon1,
                                    @ForAll double lat2, @ForAll double lon2) {
        lat1 = (lat1 % 80) - 40; // constrain
        lon1 = (lon1 % 160) - 80;
        lat2 = ((lat2 % 80) - 40) + 0.1;
        lon2 = ((lon2 % 160) - 80) + 0.1;
        LineString line = gf.createLineString(new Coordinate[]{
                new Coordinate(lon1, lat1),
                new Coordinate(lon2, lat2)
        });
        Geometry corridor = service.build(line, Mode.DRIVING_CAR);
        assertTrue(corridor.isValid());
        assertTrue(corridor.getArea() > 0);
    }
}
