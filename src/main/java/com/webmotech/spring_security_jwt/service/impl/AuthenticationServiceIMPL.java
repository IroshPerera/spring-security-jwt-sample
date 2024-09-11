package com.webmotech.spring_security_jwt.service.impl;


import com.webmotech.spring_security_jwt.dto.JwtAuthResponse;
import com.webmotech.spring_security_jwt.dto.SignIn;
import com.webmotech.spring_security_jwt.dto.SignUp;
import com.webmotech.spring_security_jwt.dto.UserDTO;
import com.webmotech.spring_security_jwt.model.UserEntity;
import com.webmotech.spring_security_jwt.repository.UserRepo;
import com.webmotech.spring_security_jwt.service.AuthenticationService;
import com.webmotech.spring_security_jwt.service.JWTService;
import com.webmotech.spring_security_jwt.util.mapper.GlobalMapper;
import com.webmotech.spring_security_jwt.util.mapper.exeption.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceIMPL implements AuthenticationService {

    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final GlobalMapper mapping;

    //utils

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtAuthResponse signIn(SignIn request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var jwt = jwtService.generateToken(user);
        user.setPassword(null);
        return JwtAuthResponse.builder().token(jwt).userDTO(mapping.toUserDTO(user)).build();
    }

    @Override
    public JwtAuthResponse signUp(SignUp signUp) {
        UserDTO buildUser = UserDTO.builder()
                .id(UUID.randomUUID().toString())
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .role(signUp.getRole())
                .build();
        var savedUser = userRepo.save(mapping.toUser(buildUser));
       var genToken =  jwtService.generateToken(savedUser);
       buildUser.setPassword(null);
        return JwtAuthResponse.builder().token(genToken).userDTO(buildUser).build();
    }

    @Override
    public String refreshToken(String token) {
        String userName = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwtService.generateToken(user)).build().getToken();
    }


}
