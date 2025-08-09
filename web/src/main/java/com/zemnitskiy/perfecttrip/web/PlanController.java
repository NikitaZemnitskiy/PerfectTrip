package com.zemnitskiy.perfecttrip.web;

import com.zemnitskiy.perfecttrip.planner.PlanRequest;
import com.zemnitskiy.perfecttrip.planner.PlanResponse;
import com.zemnitskiy.perfecttrip.planner.PlannerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlanController {

    private final PlannerService plannerService;

    public PlanController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/plan")
    public PlanResponse plan(@Valid @RequestBody PlanRequest request) {
        return plannerService.plan(request);
    }
}
