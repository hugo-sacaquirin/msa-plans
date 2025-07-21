package com.sipeip.service.mapper;


import com.sipeip.domain.dto.PlanWithInstitutionalObjectivesView;
import com.sipeip.domain.dto.PlanWithProgramsView;
import com.sipeip.domain.entity.Plan;
import com.sipeip.infrastructure.input.adapter.rest.models.InstitutionalObjectivesSummary;
import com.sipeip.infrastructure.input.adapter.rest.models.PlanResponseSummary;
import com.sipeip.infrastructure.input.adapter.rest.models.ProgramSummary;
import com.sipeip.repository.PlanInstitutionalObjectiveRepository;
import com.sipeip.repository.PlanProgramsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanMapper {
    private final PlanProgramsRepository planProgramsRepository;
    private final PlanInstitutionalObjectiveRepository planInstitutionalObjectiveRepository;

    public PlanResponseSummary toGoalResponse(Plan project) {
        if (project == null) {
            return null;
        }
        PlanResponseSummary goalResponse = new PlanResponseSummary();
        goalResponse.setId(project.getId());
        goalResponse.setName(project.getName());
        goalResponse.setVersion(project.getVersion());
        goalResponse.setPeriodStart(String.valueOf(project.getPeriodStart()));
        goalResponse.setPeriodEnd(String.valueOf(project.getPeriodEnd()));
        goalResponse.setPlanStatus(project.getPlanStatus());
        goalResponse.setStatus(project.getStatus());
        goalResponse.setCreatedAt(String.valueOf(project.getCreatedAt()));
        goalResponse.setUpdatedAt(String.valueOf(project.getUpdatedAt()));
        List<ProgramSummary> goalResponseList = new ArrayList<>();
        for (PlanWithProgramsView projectGoalView : planProgramsRepository.findPlanWithProgramsByPlanId(goalResponse.getId())) {
            ProgramSummary response = new ProgramSummary();
            response.setId(projectGoalView.getPlanProgramId());
            response.setName(projectGoalView.getProgramName());
            response.setScope(projectGoalView.getProgramScope());
            goalResponseList.add(response);
        }
        goalResponse.setPrograms(goalResponseList);

        List<InstitutionalObjectivesSummary> institutionalObjectivesSummaries = new ArrayList<>();
        for (PlanWithInstitutionalObjectivesView planWithInstitutionalObjectivesView : planInstitutionalObjectiveRepository.findPlanWithObjectivesByPlanId(goalResponse.getId())) {
            InstitutionalObjectivesSummary response = new InstitutionalObjectivesSummary();
            response.setId(planWithInstitutionalObjectivesView.getInstitutionalObjectiveId());
            response.setStrategicObjective(planWithInstitutionalObjectivesView.getStrategicObjectiveName());
            response.setPndObjective(planWithInstitutionalObjectivesView.getPndObjectiveName());
            response.setOdsObjective(planWithInstitutionalObjectivesView.getOdsObjectiveName());
            institutionalObjectivesSummaries.add(response);
        }
        goalResponse.setInstitutionalObjectives(institutionalObjectivesSummaries);

        return goalResponse;
    }

    public List<PlanResponseSummary> toGoalResponseFromGoal(List<Plan> entitiesList) {
        return entitiesList.stream()
                .map(this::toGoalResponse)
                .toList();
    }
}