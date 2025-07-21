package com.sipeip.service;

import com.sipeip.domain.dto.PlanWithInstitutionalObjectivesView;
import com.sipeip.domain.dto.PlanWithProgramsView;
import com.sipeip.domain.entity.Plan;
import com.sipeip.infrastructure.input.adapter.rest.models.InstitutionalObjectivesSummary;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResponseSummary;
import com.sipeip.infrastructure.input.adapter.rest.models.ProgramSummary;
import com.sipeip.repository.PlanInstitutionalObjectiveRepository;
import com.sipeip.repository.PlanProgramsRepository;
import com.sipeip.service.mapper.PlanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlanMapperTest {

    @Mock
    private PlanProgramsRepository planProgramsRepository;
    @Mock
    private PlanInstitutionalObjectiveRepository planInstitutionalObjectiveRepository;

    private PlanMapper planMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        planMapper = new PlanMapper(planProgramsRepository, planInstitutionalObjectiveRepository);
    }

    @Test
    void shouldMapNullPlanToNullResponse() {
        PlanResponseSummary result = planMapper.toGoalResponse(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapPlanToPlanResponseSummaryWithDetails() {
        // Arrange
        Plan plan = Plan.builder()
                .id(100)
                .name("Plan Test")
                .version("1.0")
                .periodStart(LocalDate.of(2024, 1, 1))
                .periodEnd(LocalDate.of(2024, 12, 31))
                .planStatus("CREADO")
                .status("ACTIVO")
                .createdAt(LocalDateTime.of(2024, 1, 2, 10, 30))
                .updatedAt(LocalDateTime.of(2024, 1, 3, 11, 0))
                .build();

        // Fake Programs
        PlanWithProgramsView programView = mock(PlanWithProgramsView.class);
        when(programView.getPlanProgramId()).thenReturn(11);
        when(programView.getProgramName()).thenReturn("Prog 1");
        when(programView.getProgramScope()).thenReturn("Scope 1");
        when(planProgramsRepository.findPlanWithProgramsByPlanId(100))
                .thenReturn(List.of(programView));

        // Fake Objectives
        PlanWithInstitutionalObjectivesView objView = mock(PlanWithInstitutionalObjectivesView.class);
        when(objView.getInstitutionalObjectiveId()).thenReturn(21);
        when(objView.getStrategicObjectiveName()).thenReturn("Obj E");
        when(objView.getPndObjectiveName()).thenReturn("PND X");
        when(objView.getOdsObjectiveName()).thenReturn("ODS Y");
        when(planInstitutionalObjectiveRepository.findPlanWithObjectivesByPlanId(100))
                .thenReturn(List.of(objView));

        // Act
        PlanResponseSummary result = planMapper.toGoalResponse(plan);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100);
        assertThat(result.getName()).isEqualTo("Plan Test");
        assertThat(result.getVersion()).isEqualTo("1.0");
        assertThat(result.getPeriodStart()).isEqualTo("2024-01-01");
        assertThat(result.getPeriodEnd()).isEqualTo("2024-12-31");
        assertThat(result.getPlanStatus()).isEqualTo("CREADO");
        assertThat(result.getStatus()).isEqualTo("ACTIVO");

        assertThat(result.getPrograms()).hasSize(1);
        ProgramSummary prog = result.getPrograms().get(0);
        assertThat(prog.getId()).isEqualTo(11);
        assertThat(prog.getName()).isEqualTo("Prog 1");
        assertThat(prog.getScope()).isEqualTo("Scope 1");

        assertThat(result.getInstitutionalObjectives()).hasSize(1);
        InstitutionalObjectivesSummary obj = result.getInstitutionalObjectives().get(0);
        assertThat(obj.getId()).isEqualTo(21);
        assertThat(obj.getStrategicObjective()).isEqualTo("Obj E");
        assertThat(obj.getPndObjective()).isEqualTo("PND X");
        assertThat(obj.getOdsObjective()).isEqualTo("ODS Y");
    }

    @Test
    void shouldMapListOfPlansToListOfResponses() {
        Plan plan1 = Plan.builder().id(1).name("P1").build();
        Plan plan2 = Plan.builder().id(2).name("P2").build();
        when(planProgramsRepository.findPlanWithProgramsByPlanId(anyInt())).thenReturn(List.of());
        when(planInstitutionalObjectiveRepository.findPlanWithObjectivesByPlanId(anyInt())).thenReturn(List.of());

        List<PlanResponseSummary> list = planMapper.toGoalResponseFromGoal(List.of(plan1, plan2));

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(1).getId()).isEqualTo(2);
    }
}
