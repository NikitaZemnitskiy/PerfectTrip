package com.zemnitskiy.perfecttrip.planner;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlanRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequestShouldPassValidation() {
        PlanRequest request = new PlanRequest(
                new GeoPoint(50.0, 10.0),
                new GeoPoint(51.0, 11.0),
                Mode.DRIVING_CAR,
                60,
                List.of("museums")
        );
        Set<ConstraintViolation<PlanRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidLatShouldFailValidation() {
        PlanRequest request = new PlanRequest(
                new GeoPoint(100.0, 10.0),
                new GeoPoint(51.0, 11.0),
                Mode.DRIVING_CAR,
                60,
                List.of("museums")
        );
        Set<ConstraintViolation<PlanRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
