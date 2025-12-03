package com.jobportal.controller;

import com.jobportal.model.JobPost;
import com.jobportal.model.User;
import com.jobportal.repository.JobPostRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobPostRepository jobRepo;
    private final UserRepository userRepo;
    public JobController(JobPostRepository j, UserRepository u){ this.jobRepo=j; this.userRepo=u; }

    @GetMapping
    public Page<JobPost> list(@RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="10") int size,
                              @RequestParam(defaultValue="") String q) {
        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if(q==null || q.isBlank()) return jobRepo.findAll(p);
        return jobRepo.findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(q,q,p);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody JobPost job, Authentication auth) {
        // only employer allowed by security config
        String username = auth.getName();
        User employer = userRepo.findByUsername(username).orElseThrow();
        job.setEmployer(employer);
        job.setCreatedAt(java.time.LocalDateTime.now());
        jobRepo.save(job);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return jobRepo.findById(id).map(j->ResponseEntity.ok(j)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JobPost req, Authentication auth){
        return jobRepo.findById(id).map(job -> {
            if (!job.getEmployer().getUsername().equals(auth.getName())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            job.setTitle(req.getTitle());
            job.setDescription(req.getDescription());
            job.setLocation(req.getLocation());
            job.setSalary(req.getSalary());
            jobRepo.save(job);
            return ResponseEntity.ok(job);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        return jobRepo.findById(id).map(job -> {
            if (!job.getEmployer().getUsername().equals(auth.getName())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            jobRepo.delete(job);
            return ResponseEntity.ok(Map.of("message","deleted"));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<?> myJobs(Authentication auth){
        var u = userRepo.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(jobRepo.findByEmployerId(u.getId()));
    }
}
