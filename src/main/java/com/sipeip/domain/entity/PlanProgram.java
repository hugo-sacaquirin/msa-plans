package com.sipeip.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_programas")
@Builder // Lombok annotation to generate a builder for the class
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_programas")
    private Integer id;

    @Column(name = "id_plan", nullable = false)
    private Integer planId;

    @Column(name = "id_programa", nullable = false)
    private Integer programId;
}
