package com.zemnitskiy.perfecttrip.planner;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PlanRequest(
        @Valid @NotNull GeoPoint start,
        @Valid @NotNull GeoPoint end,
        @NotNull Mode mode,
        @Min(1) int timeBudgetMin,
        @NotEmpty List<String> interests
) {
}
