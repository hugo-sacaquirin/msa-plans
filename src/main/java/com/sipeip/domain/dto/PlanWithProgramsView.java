package com.sipeip.domain.dto;

import java.time.LocalDateTime;

public interface PlanWithProgramsView {
    Integer getPlanProgramId();

    Integer getProgramId();

    String getProgramName();

    String getProgramDescription();

    String getProgramScope();

    String getProgramStatus();

    String getProgramResponsible();

    LocalDateTime getProgramCreatedAt();

    LocalDateTime getProgramUpdatedAt();
}

