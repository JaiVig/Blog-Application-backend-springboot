package com.crusty.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String email, String pwd);

    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);
}
