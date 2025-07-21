package com.sipeip.service;

import com.sipeip.infrastructure.input.adapter.rest.models.PlanApprovalRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanPagedResponse;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResultResponse;

public interface PlanService {
    PlanResultResponse createPlan(PlanRequest request);

    PlanResultResponse updatePlan(Integer id, PlanRequest request);

    void deletePlanById(Integer id);

    PlanPagedResponse getPagedPlan(Integer page, Integer size, String statusPlan);

    PlanPagedResponse searchPlan(Integer page, Integer size, String name, String version, String type, String statusPlan);

    void updatePlanStatusAndObservation(Integer id, PlanApprovalRequest planApprovalRequest);
}
