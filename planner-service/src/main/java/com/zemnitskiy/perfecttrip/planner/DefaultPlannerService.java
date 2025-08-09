package com.zemnitskiy.perfecttrip.planner;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPlannerService implements PlannerService {
    @Override
    public PlanResponse plan(PlanRequest request) {
        return new PlanResponse(List.of());
    }
}
