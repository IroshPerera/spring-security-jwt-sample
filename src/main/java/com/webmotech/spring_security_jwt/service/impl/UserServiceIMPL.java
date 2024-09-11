package com.webmotech.spring_security_jwt.service.impl;

import com.webmotech.spring_security_jwt.repository.UserRepo;
import com.webmotech.spring_security_jwt.service.UserService;
import com.webmotech.spring_security_jwt.util.mapper.GlobalMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {


    private final UserRepo userRepo;
    private final GlobalMapper mapping;


    @Override
    public UserDetailsService userDetailsService() {
        return username ->
                userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}

