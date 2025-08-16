package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private UserRepository userRepository;

    public Optional<User> getByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
