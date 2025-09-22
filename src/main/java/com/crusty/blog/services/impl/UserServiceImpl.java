package com.crusty.blog.services.impl;

import com.crusty.blog.domain.entities.User;
import com.crusty.blog.repositories.UserRepo;
import com.crusty.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepo userRepo;

    @Override
    public User getUserById(UUID uuid) {
        System.out.println("yooo  "+uuid);
        return userRepo.findById(uuid).orElseThrow(() -> new EntityNotFoundException("No user exists with id "+ uuid));
    }
}
