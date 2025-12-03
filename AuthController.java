package com.jobportal.controller;

import com.jobportal.config.JwtUtil;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    public AuthController(UserRepository r, PasswordEncoder e, JwtUtil j){ this.userRepo=r; this.encoder=e; this.jwtUtil=j; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body){
        String username = body.get("username"), password = body.get("password");
        String role = body.getOrDefault("role","JOB_SEEKER");
        if (userRepo.findByUsername(username).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("User exists");
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String username=body.get("username"), password=body.get("password");
        var opt = userRepo.findByUsername(username);
        if(opt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid");
        User u = opt.get();
        if(!encoder.matches(password, u.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid");
        String token = jwtUtil.generateToken(u.getUsername(), u.getRole());
        return ResponseEntity.ok(Map.of("token", token, "role", u.getRole()));
    }
}
