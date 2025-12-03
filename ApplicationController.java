package com.jobportal.controller;

import com.jobportal.model.*;
import com.jobportal.repository.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final JobApplicationRepository appRepo;
    private final JobPostRepository jobRepo;
    private final UserRepository userRepo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ApplicationController(JobApplicationRepository a, JobPostRepository j, UserRepository u){
        this.appRepo=a; this.jobRepo=j; this.userRepo=u;
    }

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(@RequestParam Long jobId,
                                   @RequestParam(required=false) String coverLetter,
                                   @RequestPart MultipartFile resume,
                                   Authentication auth) throws IOException {
        var job = jobRepo.findById(jobId).orElseThrow();
        var seeker = userRepo.findByUsername(auth.getName()).orElseThrow();

        // save file locally
        String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(resume.getOriginalFilename());
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Path filePath = dir.resolve(filename);
        try (InputStream in = resume.getInputStream()) { Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING); }

        JobApplication appl = new JobApplication();
        appl.setJob(job);
        appl.setSeeker(seeker);
        appl.setResumePath(filePath.toString());
        appl.setCoverLetter(coverLetter);
        appl.setAppliedAt(java.time.LocalDateTime.now());
        appRepo.save(appl);
        return ResponseEntity.ok(Map.of("message","applied"));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> byJob(@PathVariable Long jobId, Authentication auth){
        var job = jobRepo.findById(jobId).orElseThrow();
        // only employer who owns job or admin
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        if(!user.getRole().equals("ADMIN") && !job.getEmployer().getId().equals(user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(appRepo.findByJobId(jobId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> myApplications(Authentication auth) {
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(appRepo.findBySeekerId(user.getId()));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String,String> body, Authentication auth) {
        var appl = appRepo.findById(id).orElseThrow();
        var job = appl.getJob();
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        if(!user.getRole().equals("ADMIN") && !job.getEmployer().getId().equals(user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        appl.setStatus(body.getOrDefault("status", appl.getStatus()));
        appRepo.save(appl);
        return ResponseEntity.ok(appl);
    }
}
