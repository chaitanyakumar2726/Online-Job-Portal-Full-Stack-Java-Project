package com.jobportal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_application")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="job_id")
    private JobPost job;

    @ManyToOne
    @JoinColumn(name="seeker_id")
    private User seeker;

    private String resumePath;
    @Column(length=2000)
    private String coverLetter;
    private String status = "APPLIED"; // APPLIED, REVIEWED, REJECTED, SELECTED
    private LocalDateTime appliedAt = LocalDateTime.now();
}
