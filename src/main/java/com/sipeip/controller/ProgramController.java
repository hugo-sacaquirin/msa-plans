package com.sipeip.controller;


import com.sipeip.infrastructure.input.adapter.rest.PlansApi;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanApprovalRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanPagedResponse;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResultResponse;
import com.sipeip.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgramController implements PlansApi {
    private final PlanService planService;

    @Override
    public ResponseEntity<PlanResultResponse> createPlan(PlanRequest planRequest) {
        return new ResponseEntity<>(planService.createPlan(planRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deactivatePlan(Integer id) {
        planService.deletePlanById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PlanPagedResponse> getPagedPendingReviewPlans(Integer page, Integer size, String statusPlan) {
        return new ResponseEntity<>(planService.getPagedPlan(page, size, statusPlan), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlanPagedResponse> getPagedPlans(Integer page, Integer size, String statusPlan) {
        return new ResponseEntity<>(planService.getPagedPlan(page, size, statusPlan), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlanPagedResponse> searchPendingReviewPlans(Integer page, Integer size, String name, String version, String type, String statusPlan) {
        return new ResponseEntity<>(planService.searchPlan(page, size, name, version, type, statusPlan), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlanPagedResponse> searchPlans(Integer page, Integer size, String name, String version, String type, String statusPlan) {
        return new ResponseEntity<>(planService.searchPlan(page, size, name, version, type, statusPlan), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> sendPlanForReview(Integer id, PlanApprovalRequest planApprovalRequest) {
        planService.updatePlanStatusAndObservation(id, planApprovalRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PlanResultResponse> updatePlan(Integer id, PlanRequest planRequest) {
        return new ResponseEntity<>(planService.updatePlan(id, planRequest), HttpStatus.CREATED);
    }
}
