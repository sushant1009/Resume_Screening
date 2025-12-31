package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.AppUser;
import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Security.JwtUtil;
import com.example.Resume_Screening_Backend.Service.RecruiterService;
import com.example.Resume_Screening_Backend.Service.UserService;
import com.example.Resume_Screening_Backend.dto.AuthResponse;
import com.example.Resume_Screening_Backend.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private final UserService userService;
//    private final RecruiterService recruiterService;
//    private final PasswordEncoder encoder;
//
//    @PostMapping("/signup/user")
//    public ResponseEntity<?> signupUser(@RequestBody User user) {
//        System.out.println("Recieved");
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//        return ResponseEntity.ok(userService.save(user));
//    }
//
//    @PostMapping("/signup/recruiter")
//    public ResponseEntity<?> signupRecruiter(@RequestBody Recruiter recruiter) {
//        System.out.println("Recieved");
//        recruiter.setPassword(encoder.encode(recruiter.getPassword()));
//        recruiter.setCreatedAt(LocalDateTime.now());
//        return ResponseEntity.ok(recruiterService.save(recruiter));
//    }
//
//
//    @PostMapping("/login/user")
//    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
//        User user = userService.getByUsername(credentials.get("username"))
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (encoder.matches(credentials.get("password"), user.getPassword())) {
//            return ResponseEntity.ok(user);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
//    @PostMapping("/login/recruiter")
//    public ResponseEntity<?> loginRecruiter(@RequestBody Map<String, String> credentials) {
//        Recruiter recruiter = recruiterService.getRecruiterByUsername(credentials.get("username"))
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (encoder.matches(credentials.get("password"), recruiter.getPassword())) {
//            return ResponseEntity.ok(recruiter);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
//
//}
//
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RecruiterService recruiterService;
    private final PasswordEncoder encoder;

    // ---------------- SIGNUP ----------------

    @PostMapping("/signup/user")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        System.out.println(user.toString());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/signup/recruiter")
    public ResponseEntity<?> signupRecruiter(@RequestBody Recruiter recruiter) {
        recruiter.setPassword(encoder.encode(recruiter.getPassword()));
        recruiter.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(recruiterService.save(recruiter));
    }

    // ---------------- LOGIN (JWT) ----------------

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        // 1️⃣ Let Spring Security authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        // 2️⃣ Fetch AppUser (User or Recruiter)
        AppUser appUser =
                userService.getByUsername(request.getUsername())
                        .map(u -> (AppUser) u)
                        .orElseGet(() ->
                                recruiterService.getRecruiterByUsername(
                                                request.getUsername())
                                        .orElseThrow(() ->
                                                new RuntimeException("User not found"))
                        );

        // 3️⃣ Generate JWT
        String token = jwtUtil.generateToken(appUser);

        // 4️⃣ Return token + role
        if(!token.isEmpty()) {
            return ResponseEntity.ok(
                    new AuthResponse(token, appUser.getRole())
            );
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    
}
