package com.crusty.blog.domain.dtos;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private long expiresIn;
}
