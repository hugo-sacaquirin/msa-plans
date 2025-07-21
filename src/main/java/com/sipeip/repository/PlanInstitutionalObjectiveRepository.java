package com.sipeip.repository;

import com.sipeip.domain.dto.PlanWithInstitutionalObjectivesView;
import com.sipeip.domain.entity.PlanInstitutionalObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanInstitutionalObjectiveRepository extends JpaRepository<PlanInstitutionalObjective, Integer> {
    @Query(value = """
            SELECT
                poi.id_plan_institucionales AS institutionalObjectiveId,
                poi.id_alineacion AS alignmentId,
                ao.id_objetivo_estrategico AS strategicObjectiveId,
                oe.nombre AS strategicObjectiveName,
                ao.id_objetivo_pnd AS pndObjectiveId,
                opnd.nombre AS pndObjectiveName,
                ao.id_objetivo_ods AS odsObjectiveId,
                oods.nombre AS odsObjectiveName,
                ao.id_entidad AS entityId,
                e.nombre AS entityName,
                ao.estado AS alignmentStatus,
                ao.fecha_creacion AS alignmentCreatedAt,
                ao.fecha_actualizacion AS alignmentUpdatedAt
            FROM planes pl
            JOIN plan_objetivos_institucionales poi ON pl.id_plan = poi.id_plan
            JOIN alineaciones_objetivos ao ON poi.id_alineacion = ao.id_alineacion
            JOIN objetivos oe ON ao.id_objetivo_estrategico = oe.id_objetivo
            JOIN objetivos opnd ON ao.id_objetivo_pnd = opnd.id_objetivo
            JOIN objetivos oods ON ao.id_objetivo_ods = oods.id_objetivo
            JOIN entidades e ON ao.id_entidad = e.id_entidad
            WHERE pl.id_plan = :planId
            ORDER BY poi.id_plan_institucionales ASC
            """, nativeQuery = true)
    List<PlanWithInstitutionalObjectivesView> findPlanWithObjectivesByPlanId(@Param("planId") Integer planId);

}