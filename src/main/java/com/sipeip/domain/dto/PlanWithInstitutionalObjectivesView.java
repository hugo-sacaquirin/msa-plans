package com.sipeip.domain.dto;

import java.time.LocalDateTime;

public interface PlanWithInstitutionalObjectivesView {
    Integer getInstitutionalObjectiveId();

    Integer getAlignmentId();

    Integer getStrategicObjectiveId();

    String getStrategicObjectiveName();

    Integer getPndObjectiveId();

    String getPndObjectiveName();

    Integer getOdsObjectiveId();

    String getOdsObjectiveName();

    Integer getEntityId();

    String getEntityName();

    String getAlignmentStatus();

    LocalDateTime getAlignmentCreatedAt();

    LocalDateTime getAlignmentUpdatedAt();
}
