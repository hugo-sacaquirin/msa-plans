package com.sipeip.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_objetivos_institucionales")
@Builder // Lombok annotation to generate a builder for the class
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanInstitutionalObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_institucionales")
    private Integer id;

    @Column(name = "id_plan", nullable = false)
    private Integer planId;

    @Column(name = "id_alineacion", nullable = false)
    private Integer alignmentId;
}
