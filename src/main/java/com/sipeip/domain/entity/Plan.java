package com.sipeip.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planes")
@Builder // Lombok annotation to generate a builder for the class
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String name;

    @Column(name = "version", length = 50, nullable = false)
    private String version;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodStart;

    @Column(name = "periodo_fin", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "estado_plan", length = 15, nullable = false)
    private String planStatus;

    @Column(name = "estado", length = 10, nullable = false)
    private String status;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "observacion")
    private String observation;
}
