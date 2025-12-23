package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.AppUser;
import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.RecruiterRepository;
import com.example.Resume_Screening_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RecruiterRepository recruiterRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // 1. Check normal user
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return buildUserDetails(user.get());
        }

        // 2. Check recruiter
        Optional<Recruiter> recruiter = recruiterRepo.findByUsername(username);
        if (recruiter.isPresent()) {
            return buildUserDetails(recruiter.get());
        }

        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(AppUser appUser) {
        return org.springframework.security.core.userdetails.User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }
}
