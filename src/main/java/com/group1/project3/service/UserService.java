package com.group1.project3.service;

import com.group1.project3.DTO.UpdateUserAccountRequest;
import com.group1.project3.entity.User;
import com.group1.project3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {return userRepository.findAll(); }

    public User getUserById(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. "));
    }

    public User updateInfo(UUID userId, UpdateUserAccountRequest request){



        User user = getUserById(userId);

        if (request.username() != null ){
            if(request.username().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username can not be blank.");
            }


            Optional<User> check = userRepository.findByUsername(request.username());
            if (check.isPresent() && !check.get().getId().equals(userId)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists. Must be unique");

            user.setUsername(request.username());

        }

        if (request.email() != null){
            if(request.email().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email can not be blank.");
            }

            if (!request.email().toLowerCase().endsWith(".edu")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must be a .edu address");


            Optional<User> check = userRepository.findByEmail(request.email());
            if (check.isPresent() && !check.get().getId().equals(userId)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already tied to an account. Must be unique");

            user.setEmail(request.email());
        }

        if (request.password() != null) {

            if(request.password().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can not be blank");



            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return userRepository.save(user);
    }
}
