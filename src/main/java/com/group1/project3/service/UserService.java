package com.group1.project3.service;

import com.group1.project3.DTO.GetUserResponseRequest;
import com.group1.project3.DTO.UpdateUserAccountRequest;
import com.group1.project3.DTO.UpdateUserPermissionRequest;
import com.group1.project3.OAuth.OAuthUserAttributesResolver;
import com.group1.project3.entity.User;
import com.group1.project3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthUserAttributesResolver oAuthUserAttributesResolver;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OAuthUserAttributesResolver oAuthUserAttributesResolver) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.oAuthUserAttributesResolver =  oAuthUserAttributesResolver;
    }

    public List<GetUserResponseRequest> getAllUsers() {

        List<User> users = userRepository.findAll();


        return users.stream()
                .map( user -> new GetUserResponseRequest(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPermission()
                )).toList();

    }

    public GetUserResponseRequest getUserById(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. "));

        return new GetUserResponseRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPermission()
        );
    }

    public User updateInfo(UUID userId, UpdateUserAccountRequest request){



        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. "));

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

    public User updatePermission(UUID userId, UpdateUserPermissionRequest request){

        User promote = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        promote.setPermission(request.permission());

        return userRepository.save(promote);
    }

    public User delete(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.deleteById(userId);
        return user;
    }

    public User getFromOAuth(OAuth2User principal){
        if(principal == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required.");

        Map<String, Object> attrs = principal.getAttributes();
        String provider = oAuthUserAttributesResolver.resolveProvider(attrs);
        String subject = oAuthUserAttributesResolver.resolveSubject(provider, attrs);

        return userRepository.findByOauthProviderAndOAuthSubject(provider, subject)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Authenticated user not found."));


    }

}
