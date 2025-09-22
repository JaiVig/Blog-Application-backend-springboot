package com.crusty.blog.configs;


import com.crusty.blog.domain.entities.User;
import com.crusty.blog.repositories.UserRepo;
import com.crusty.blog.security.BlogUserDetails;
import com.crusty.blog.security.BlogUserDetailsService;
import com.crusty.blog.security.JwtAuthFilter;
import com.crusty.blog.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Bean
    public JwtAuthFilter jwtAuthFilter(AuthenticationService AuthenticationService)
    {
        return new JwtAuthFilter(AuthenticationService);
    }
//.requestMatchers(HttpMethod.GET,"/api/v1/posts/drafts").authenticated()
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter) throws Exception {

        httpSecurity.authorizeHttpRequests(
                a ->a
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
                                .anyRequest().authenticated())
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(s->s.
                                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        //The login traffic now goes to /api/v1/auth/login, not /login.
        //
        //UsernamePasswordAuthenticationFilter never even sees this request, because it’s not mapped to its URL (/login).
        //
        //Instead, the DispatcherServlet routes it straight to your AuthController.

        //UsernamePasswordAuthenticationFilter class extracts UN and pwd from the login request and passes it on to AuthenticationManager which calls UserDetailsService.loadUserByUsername(username) which we wrote to fetch user from db, then it checks the info rcvd from client vs the info in db then furthers the authentication step.This is the only place password verification happens.
        //This filter only triggers on POST /login (or whatever URL you configure via .loginProcessingUrl("/something")).
        //WE ARE NOT USING THIS FILTER. SINCE WE HAVE OUR OWN AUTHCONTROLLER CLASS, THAT REPLACES UsernamePasswordAuthenticationFilter

        /*
        Why both exist

        Login time (UsernamePasswordAuthenticationFilter or your custom controller):

        User proves who they are by giving password.

        Password gets checked against DB.

                If correct, you issue a JWT.

                Subsequent requests (JwtAuthFilter):

        No password involved.

                Just check the token integrity.

        Load user details to populate the SecurityContext.
                */

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception
    {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService UDS(UserRepo userRepo)
    {
        BlogUserDetailsService blogUserDetailsService= new BlogUserDetailsService(userRepo);
        String email = "user@test.com";
        String pwd = "password";
        userRepo.findByEmail(email).orElseGet(()->
        {
            User newuser = User.builder().name("test user").email(email).password(passwordEncoder().encode(pwd)).build();
            return userRepo.save(newuser);
        });
        return blogUserDetailsService;
    }


}

//"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3QuY29tIiwiaWF0IjoxNzU1ODU1NDAzLCJleHAiOjE3NTU5NDE4MDN9.kngb0_8FgvG_3xQSkhOYQ_HCy758KKHrfGBJsasE2Rw"