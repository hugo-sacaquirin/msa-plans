package com.sipeip.repository;

import com.sipeip.domain.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByPlanStatus(String planStatus);

    List<Plan> findByNameAndPlanStatus(String name, String planStatus);

    List<Plan> findByVersionAndPlanStatus(String version, String planStatus);
    @Modifying
    @Query(value = "UPDATE planes SET estado_plan = :estadoPlan, observacion = :observacion WHERE id_plan = :planId", nativeQuery = true)
    void updatePlanStatusAndObservation(@Param("estadoPlan") String statePlan,
                                        @Param("observacion") String observation,
                                        @Param("planId") Integer planId);
}