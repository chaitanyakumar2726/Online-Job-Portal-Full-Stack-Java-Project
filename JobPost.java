package com.jobportal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_post")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class JobPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 4000)
    private String description;
    private String location;
    private String company;
    private String salary;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String status = "ACTIVE"; // ACTIVE, CLOSED

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;
}
