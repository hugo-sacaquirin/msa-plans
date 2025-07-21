package com.sipeip.repository;

import com.sipeip.domain.dto.PlanWithProgramsView;
import com.sipeip.domain.entity.PlanProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanProgramsRepository extends JpaRepository<PlanProgram, Integer> {
    @Query(value = """
            SELECT
                pp.id_plan_programas AS planProgramId,
                pr.id_programa AS programId,
                pr.nombre AS programName,
                pr.descripcion AS programDescription,
                pr.alcance AS programScope,
                pr.estado AS programStatus,
                pr.responsable AS programResponsible,
                pr.fecha_creacion AS programCreatedAt,
                pr.fecha_actualizacion AS programUpdatedAt
            FROM planes pl
            JOIN plan_programas pp ON pl.id_plan = pp.id_plan
            JOIN programas pr ON pp.id_programa = pr.id_programa
            WHERE pl.id_plan = :planId
            ORDER BY pr.nombre ASC
            """, nativeQuery = true)
    List<PlanWithProgramsView> findPlanWithProgramsByPlanId(@Param("planId") Integer planId);
}