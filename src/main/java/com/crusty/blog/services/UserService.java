package com.crusty.blog.services;

import com.crusty.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID uuid);
}
