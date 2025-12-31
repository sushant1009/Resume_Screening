package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Service.OtpService;
import com.example.Resume_Screening_Backend.Service.RecruiterService;
import com.example.Resume_Screening_Backend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    private final OtpService otpService;
    private final UserService userService;
    private final RecruiterService recruiterService;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    public OtpController(OtpService otpService, UserService userService, RecruiterService recruiterService, PasswordEncoder encoder) {
        this.otpService = otpService;
        this.userService = userService;
        this.recruiterService = recruiterService;
        this.passwordEncoder = encoder;
    }

    // Send OTP
    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        if(userService.existEmail(email) || recruiterService.existEmail(email))
        {
            otpService.sendOtp(email);
            return ResponseEntity.ok("OTP sent successfully");
        }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
    }

    // Verify OTP
    @PostMapping("/otp/verify")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean verified = otpService.verifyOtp(email, otp);

        if (verified) {
            verifiedEmails.add(email);
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }

    @PutMapping("/change-pass")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String newPassword) {

        if (!verifiedEmails.contains(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("OTP verification required");
        }
        if(userService.existEmail(email)) {
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);

            verifiedEmails.remove(email); // invalidate after use

            return ResponseEntity.ok("Password changed successfully");
        }else{
            Recruiter user = recruiterService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(newPassword));
            recruiterService.save(user);

            verifiedEmails.remove(email); // invalidate after use

            return ResponseEntity.ok("Password changed successfully");
        }
    }
}

