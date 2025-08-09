package com.zemnitskiy.perfecttrip.routing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class OpenRouteServiceRoutingService implements RoutingService {

    private static final Logger log = LoggerFactory.getLogger(OpenRouteServiceRoutingService.class);

    private final WebClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenRouteServiceRoutingService(WebClient.Builder builder,
                                          @Value("${ors.api-key}") String apiKey,
                                          @Value("${ors.base-url:https://api.openrouteservice.org}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl)
                .defaultHeader("Authorization", apiKey)
                .build();
    }

    @Override
    public Route route(double startLat, double startLon, double endLat, double endLon, String profile) {
        long started = System.currentTimeMillis();
        try {
            JsonNode json = client.post()
                    .uri("/v2/directions/{profile}", profile)
                    .bodyValue(Map.of("coordinates", List.of(
                            List.of(startLon, startLat),
                            List.of(endLon, endLat)
                    )))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(Duration.ofSeconds(10));
            JsonNode feature = json.get("features").get(0);
            JsonNode geometry = feature.get("geometry");
            JsonNode summary = feature.get("properties").get("summary");
            LineString line = (LineString) new GeoJsonReader().read(mapper.writeValueAsString(geometry));
            double distance = summary.get("distance").asDouble();
            double duration = summary.get("duration").asDouble();
            return new Route(line, distance, duration);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 429) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "OpenRouteService rate limit", e);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "OpenRouteService error", e);
            }
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from OpenRouteService", e);
        } finally {
            log.info("ORS request took {} ms", System.currentTimeMillis() - started);
        }
    }
}
