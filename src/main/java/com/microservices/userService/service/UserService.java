package com.microservices.userService.service;

import com.microservices.userService.dto.UserRequest;
import com.microservices.userService.exception.UserNotFoundException;
import com.microservices.userService.model.User;
import com.microservices.userService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @CacheEvict(value = "allUsers", allEntries = true)
    public User createUser(UserRequest request){
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        return userRepository.save(user);
    }
    @Cacheable(value = "users", key = "#id")
    public User getUser(Long id) {
        System.out.println("Fetching from DB for id: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Cacheable(value = "allUsers")
    public List<User> getAllUser(){
        System.out.println("fetching all user from db");
        return userRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "users", key = "#id")
    })
    public void deleteUser(Long id){
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User with ID "+ id + " not found");
        }
    }

    @CachePut(value = "users", key = "#id")
    @CacheEvict(value = "allUsers", allEntries = true)
    public Optional<User> updateUser(Long id, UserRequest request){
        return userRepository.findById(id).map(user ->{
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            return userRepository.save(user);
        });

    }

}
