package com.zemnitskiy.perfecttrip.routing;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class OpenRouteServiceRoutingServiceTest {

    WireMockServer server;
    OpenRouteServiceRoutingService service;

    @BeforeEach
    void setup() {
        server = new WireMockServer(0);
        server.start();
        WireMock.configureFor(server.port());
        service = new OpenRouteServiceRoutingService(WebClient.builder(), "test", server.baseUrl());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void happyPath() {
        server.stubFor(post(urlPathEqualTo("/v2/directions/driving-car"))
                .willReturn(aResponse().withStatus(200).withBody("""
                    {"features":[{"geometry":{"type":"LineString","coordinates":[[0,0],[1,1]]},"properties":{"summary":{"distance":1000.0,"duration":600.0}}}]}
                """)));
        Route route = service.route(0,0,1,1,"driving-car");
        LineString geom = route.geometry();
        assertEquals(2, geom.getNumPoints());
        assertEquals(1000.0, route.distanceMeters());
        assertEquals(600.0, route.durationSeconds());
    }

    @Test
    void rateLimit() {
        server.stubFor(post(urlPathEqualTo("/v2/directions/driving-car"))
                .willReturn(aResponse().withStatus(429)));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.route(0,0,1,1,"driving-car"));
        assertEquals(503, ex.getStatusCode().value());
    }

    @Test
    void serverError() {
        server.stubFor(post(urlPathEqualTo("/v2/directions/driving-car"))
                .willReturn(aResponse().withStatus(500)));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.route(0,0,1,1,"driving-car"));
        assertEquals(502, ex.getStatusCode().value());
    }
}
