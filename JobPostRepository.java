package com.jobportal.repository;

import com.jobportal.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Page<JobPost> findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(String title, String company, Pageable p);
    List<JobPost> findByEmployerId(Long employerId);
}
