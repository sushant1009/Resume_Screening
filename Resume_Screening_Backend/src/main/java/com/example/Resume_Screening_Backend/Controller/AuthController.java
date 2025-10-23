package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.UserRepository;
import com.example.Resume_Screening_Backend.Service.RecruiterService;
import com.example.Resume_Screening_Backend.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RecruiterService recruiterService;
    private final PasswordEncoder encoder;

    @PostMapping("/signup/user")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        System.out.println("Recieved");
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/signup/recruiter")
    public ResponseEntity<?> signupRecruiter(@RequestBody Recruiter recruiter) {
        System.out.println("Recieved");
        recruiter.setPassword(encoder.encode(recruiter.getPassword()));
        recruiter.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(recruiterService.save(recruiter));
    }


    @PostMapping("/login/user")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        User user = userService.getByUsername(credentials.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(credentials.get("password"), user.getPassword())) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @PostMapping("/login/recruiter")
    public ResponseEntity<?> loginRecruiter(@RequestBody Map<String, String> credentials) {
        Recruiter recruiter = recruiterService.getRecruiterByUsername(credentials.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(credentials.get("password"), recruiter.getPassword())) {
            return ResponseEntity.ok(recruiter);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
//        User user = userService.getByUsername(credentials.get("username"))
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (encoder.matches(credentials.get("password"), user.getPassword())) {
//
//            // Example: simple token (you can replace with JWT)
//            String token = UUID.randomUUID().toString();
//
//            // Create cookie
//            Cookie cookie = new Cookie("auth_token", token);
//            cookie.setHttpOnly(true); // prevent JavaScript access
//            cookie.setSecure(false); // set true if using HTTPS
//            cookie.setPath("/");
//            cookie.setMaxAge(60 * 60); // 1 hour
//
//            // Add cookie to response
//            response.addCookie(cookie);
//
//            // Send minimal user info
//            return ResponseEntity.ok(Map.of(
//                    "message", "Login successful",
//                    "username", user.getUsername()
//            ));
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }

}

