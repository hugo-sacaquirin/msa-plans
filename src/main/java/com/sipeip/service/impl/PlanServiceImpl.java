package com.sipeip.service.impl;

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
import com.sipeip.service.PlanService;
import com.sipeip.service.mapper.PlanMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.sipeip.util.StaticValues.CREATED;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PlanProgramsRepository planProgramsRepository;
    private final PlanInstitutionalObjectiveRepository planInstitutionalObjectiveRepository;
    private final PlanMapper planMapper;

    @Override
    public PlanResultResponse createPlan(PlanRequest request) {
        Plan plan = planRepository.save(Plan.builder()
                .name(request.getName())
                .version(request.getVersion())
                .periodStart(getLocalDate(request.getPeriodStart()))
                .periodEnd(getLocalDate(request.getPeriodEnd()))
                .planStatus(request.getPlanStatus())
                .status(request.getStatus())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build());
        if (plan.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating Plan");
        }
        for (Integer idGoals : request.getProgramIds()) {
            planProgramsRepository.save(PlanProgram.builder().planId(plan.getId()).programId(idGoals).build());
        }
        for (Integer idObjectives : request.getInstitutionalObjectiveAlignmentIds()) {
            planInstitutionalObjectiveRepository.save(PlanInstitutionalObjective.builder().planId(plan.getId()).alignmentId(idObjectives).build());
        }
        return getGoalResultResponse("Project program successfully");
    }

    private static LocalDate getLocalDate(String request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(request, formatter);
    }

    @Override
    public PlanResultResponse updatePlan(Integer id, PlanRequest request) {
        Plan plan = planRepository.save(Plan.builder()
                .id(id)
                .name(request.getName())
                .version(request.getVersion())
                .periodStart(getLocalDate(request.getPeriodStart()))
                .periodEnd(getLocalDate(request.getPeriodEnd()))
                .planStatus(request.getPlanStatus())
                .status(request.getStatus())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build());
        if (plan.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating Plan");
        }
        return getGoalResultResponse("Plan updating successfully");
    }

    private static PlanResultResponse getGoalResultResponse(String message) {
        PlanResultResponse entityResultResponse = new PlanResultResponse();
        entityResultResponse.setCode(CREATED);
        entityResultResponse.setResult(message);
        return entityResultResponse;
    }

    @Override
    public void deletePlanById(Integer id) {
        planRepository.deleteById(id);
        if (planRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting plan");
        }
    }

    @Override
    public PlanPagedResponse getPagedPlan(Integer page, Integer size, String statusPlan) {
        PlanPagedResponse entityPagedResponse = new PlanPagedResponse();
        entityPagedResponse.setContent(planMapper.toGoalResponseFromGoal(planRepository.findByPlanStatus(statusPlan)));
        return entityPagedResponse;
    }

    @Override
    public PlanPagedResponse searchPlan(Integer page, Integer size, String name, String version, String type, String statusPlan) {
        PlanPagedResponse entityPagedResponse = new PlanPagedResponse();
        if (type.equals("0")) {
            entityPagedResponse.setContent(planMapper.toGoalResponseFromGoal(planRepository.findByNameAndPlanStatus(name, statusPlan)));
        } else {
            entityPagedResponse.setContent(planMapper.toGoalResponseFromGoal(planRepository.findByVersionAndPlanStatus(version, statusPlan)));
        }
        return entityPagedResponse;
    }

    @Transactional
    @Override
    public void updatePlanStatusAndObservation(Integer id, PlanApprovalRequest planApprovalRequest) {
        planRepository.updatePlanStatusAndObservation(planApprovalRequest.getStatus(), planApprovalRequest.getObservations(), id);
    }
}
