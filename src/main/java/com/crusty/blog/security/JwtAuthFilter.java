package com.crusty.blog.security;

import com.crusty.blog.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);
        try
        {
            if (token != null) {
                UserDetails userDetails = authenticationService.validateToken(token);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,                 // principal (the user object)
                                null,                        // credentials (JWT already validated, so no password needed)
                                userDetails.getAuthorities() // roles/permissions
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (userDetails instanceof BlogUserDetails blogUser) {
                    request.setAttribute("userId", blogUser.getUser().getId());
                }


            }
        } catch (Exception e) {
            //Dont throw error, let spring chain throw error itself later on
            log.warn("Rcvd invalid auth token");
        }


        filterChain.doFilter(request,response);

    }

    String extractToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;
    }
}
