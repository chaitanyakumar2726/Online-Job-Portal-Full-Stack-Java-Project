package com.jobportal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password; // store BCrypt in prod

    @Column(nullable=false)
    private String role = "JOB_SEEKER"; // JOB_SEEKER, EMPLOYER, ADMIN

    private boolean enabled = true;
}
