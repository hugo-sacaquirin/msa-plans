package com.sipeip.service;

import com.sipeip.domain.entity.Plan;
import com.sipeip.domain.entity.PlanInstitutionalObjective;
import com.sipeip.domain.entity.PlanProgram;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanApprovalRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanPagedResponse;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanRequest;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResultResponse;
import com.sipeip.repository.PlanInstitutionalObjectiveRepository;
import com.sipeip.repository.PlanProgramsRepository;
import com.sipeip.repository.PlanRepository;
import com.sipeip.service.impl.PlanServiceImpl;
import com.sipeip.service.mapper.PlanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;
    @Mock
    private PlanProgramsRepository planProgramsRepository;
    @Mock
    private PlanInstitutionalObjectiveRepository planInstitutionalObjectiveRepository;
    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePlanWhenValidRequest() {
        PlanRequest request = new PlanRequest();
        request.setName("Plan 1");
        request.setVersion("1.0");
        request.setPeriodStart("2024-01-01");
        request.setPeriodEnd("2024-12-31");
        request.setPlanStatus("CREADO");
        request.setStatus("ACTIVO");
        request.setProgramIds(Arrays.asList(1, 2));
        request.setInstitutionalObjectiveAlignmentIds(Arrays.asList(10, 20));

        Plan savedPlan = Plan.builder()
                .id(99)
                .name(request.getName())
                .version(request.getVersion())
                .periodStart(LocalDate.of(2024, 1, 1))
                .periodEnd(LocalDate.of(2024, 12, 31))
                .planStatus("CREADO")
                .status("ACTIVO")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        PlanResultResponse response = service.createPlan(request);

        assertThat(response).isNotNull();
        assertThat(response.getResult()).contains("successfully");
        verify(planRepository).save(any(Plan.class));
        verify(planProgramsRepository, times(2)).save(any(PlanProgram.class));
        verify(planInstitutionalObjectiveRepository, times(2)).save(any(PlanInstitutionalObjective.class));
    }

    @Test
    void shouldThrowExceptionWhenPlanNotCreated() {
        PlanRequest request = new PlanRequest();
        request.setPeriodEnd("2025-07-20");
        request.setPeriodStart("2025-07-20");
        request.setName("FailPlan");
        when(planRepository.save(any(Plan.class))).thenReturn(Plan.builder().build());

        assertThatThrownBy(() -> service.createPlan(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Error creating Plan");
    }

    @Test
    void shouldUpdatePlanWhenValidRequest() {
        PlanRequest request = new PlanRequest();
        request.setName("PlanMod");
        request.setVersion("2.0");
        request.setPeriodStart("2024-01-01");
        request.setPeriodEnd("2024-12-31");
        request.setPlanStatus("ACTUALIZADO");
        request.setStatus("ACTIVO");

        Plan savedPlan = Plan.builder().id(11).name("PlanMod").build();
        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        PlanResultResponse response = service.updatePlan(11, request);

        assertThat(response).isNotNull();
        assertThat(response.getResult()).contains("successfully");
        verify(planRepository).save(any(Plan.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatePlanFails() {
        PlanRequest request = new PlanRequest();
        request.setPeriodEnd("2025-07-20");
        request.setPeriodStart("2025-07-20");
        when(planRepository.save(any(Plan.class))).thenReturn(Plan.builder().build());

        assertThatThrownBy(() -> service.updatePlan(1, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Error updating Plan");
    }

    @Test
    void shouldDeletePlanWhenValidId() {
        doNothing().when(planRepository).deleteById(101);
        when(planRepository.existsById(101)).thenReturn(false);

        service.deletePlanById(101);

        verify(planRepository).deleteById(101);
        verify(planRepository).existsById(101);
    }

    @Test
    void shouldThrowExceptionWhenPlanNotDeleted() {
        doNothing().when(planRepository).deleteById(100);
        when(planRepository.existsById(100)).thenReturn(true);

        assertThatThrownBy(() -> service.deletePlanById(100))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Error deleting plan");
    }

    @Test
    void shouldGetPagedPlanWhenValidStatus() {
        when(planRepository.findByPlanStatus("CREADO")).thenReturn(Collections.emptyList());
        when(planMapper.toGoalResponseFromGoal(anyList())).thenReturn(Collections.emptyList());

        PlanPagedResponse result = service.getPagedPlan(0, 10, "CREADO");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(planRepository).findByPlanStatus("CREADO");
        verify(planMapper).toGoalResponseFromGoal(anyList());
    }

    @Test
    void shouldSearchPlanByNameWhenTypeIsZero() {
        when(planRepository.findByNameAndPlanStatus("PlanX", "CREADO")).thenReturn(Collections.emptyList());
        when(planMapper.toGoalResponseFromGoal(anyList())).thenReturn(Collections.emptyList());

        PlanPagedResponse response = service.searchPlan(0, 10, "PlanX", "1.0", "0", "CREADO");

        assertThat(response.getContent()).isEmpty();
        verify(planRepository).findByNameAndPlanStatus("PlanX", "CREADO");
    }

    @Test
    void shouldSearchPlanByVersionWhenTypeIsNotZero() {
        when(planRepository.findByVersionAndPlanStatus("2.0", "CREADO")).thenReturn(Collections.emptyList());
        when(planMapper.toGoalResponseFromGoal(anyList())).thenReturn(Collections.emptyList());

        PlanPagedResponse response = service.searchPlan(0, 10, "PlanX", "2.0", "1", "CREADO");

        assertThat(response.getContent()).isEmpty();
        verify(planRepository).findByVersionAndPlanStatus("2.0", "CREADO");
    }

    @Test
    void shouldUpdatePlanStatusAndObservation() {
        PlanApprovalRequest approvalRequest = new PlanApprovalRequest();
        approvalRequest.setStatus("EN_REVISION");
        approvalRequest.setObservations("Valid observation.");

        doNothing().when(planRepository)
                .updatePlanStatusAndObservation(anyString(), anyString(), anyInt());

        service.updatePlanStatusAndObservation(77, approvalRequest);

        verify(planRepository)
                .updatePlanStatusAndObservation("EN_REVISION", "Valid observation.", 77);
    }
}
