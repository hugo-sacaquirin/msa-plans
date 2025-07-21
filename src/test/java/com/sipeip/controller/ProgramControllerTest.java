package com.sipeip.controller;

import com.sipeip.infrastructure.input.adapter.rest.models.PlanApprovalRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanPagedResponse;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResultResponse;
import com.sipeip.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProgramControllerTest {

    @Mock
    private PlanService planService;

    @InjectMocks
    private ProgramController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePlanWhenValidRequest() {
        PlanRequest request = new PlanRequest();
        PlanResultResponse responseMock = new PlanResultResponse();
        when(planService.createPlan(request)).thenReturn(responseMock);

        ResponseEntity<PlanResultResponse> response = controller.createPlan(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseMock);
        verify(planService).createPlan(request);
    }

    @Test
    void shouldDeactivatePlanWhenValidId() {
        Integer planId = 10;

        ResponseEntity<Void> response = controller.deactivatePlan(planId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(planService).deletePlanById(planId);
    }

    @Test
    void shouldReturnPagedPendingReviewPlans() {
        PlanPagedResponse mockResult = new PlanPagedResponse();
        when(planService.getPagedPlan(1, 15, "EN_REVISION")).thenReturn(mockResult);

        ResponseEntity<PlanPagedResponse> response = controller.getPagedPendingReviewPlans(1, 15, "EN_REVISION");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResult);
        verify(planService).getPagedPlan(1, 15, "EN_REVISION");
    }

    @Test
    void shouldReturnPagedPlans() {
        PlanPagedResponse mockResult = new PlanPagedResponse();
        when(planService.getPagedPlan(0, 20, null)).thenReturn(mockResult);

        ResponseEntity<PlanPagedResponse> response = controller.getPagedPlans(0, 20, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResult);
        verify(planService).getPagedPlan(0, 20, null);
    }

    @Test
    void shouldSearchPendingReviewPlans() {
        PlanPagedResponse mockResult = new PlanPagedResponse();
        when(planService.searchPlan(0, 10, "Plan1", "v1", "tipo", "EN_REVISION")).thenReturn(mockResult);

        ResponseEntity<PlanPagedResponse> response = controller.searchPendingReviewPlans(0, 10, "Plan1", "v1", "tipo", "EN_REVISION");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResult);
        verify(planService).searchPlan(0, 10, "Plan1", "v1", "tipo", "EN_REVISION");
    }

    @Test
    void shouldSearchPlans() {
        PlanPagedResponse mockResult = new PlanPagedResponse();
        when(planService.searchPlan(0, 5, "SomePlan", "2.0", "type", "ACTIVO")).thenReturn(mockResult);

        ResponseEntity<PlanPagedResponse> response = controller.searchPlans(0, 5, "SomePlan", "2.0", "type", "ACTIVO");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResult);
        verify(planService).searchPlan(0, 5, "SomePlan", "2.0", "type", "ACTIVO");
    }

    @Test
    void shouldSendPlanForReview() {
        Integer id = 3;
        PlanApprovalRequest approval = new PlanApprovalRequest();

        ResponseEntity<Void> response = controller.sendPlanForReview(id, approval);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(planService).updatePlanStatusAndObservation(id, approval);
    }

    @Test
    void shouldUpdatePlanWhenValidRequest() {
        Integer id = 7;
        PlanRequest req = new PlanRequest();
        PlanResultResponse updated = new PlanResultResponse();
        when(planService.updatePlan(id, req)).thenReturn(updated);

        ResponseEntity<PlanResultResponse> response = controller.updatePlan(id, req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(planService).updatePlan(id, req);
    }
}
