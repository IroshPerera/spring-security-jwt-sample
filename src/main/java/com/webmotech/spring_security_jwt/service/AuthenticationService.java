package com.webmotech.spring_security_jwt.service;


import com.webmotech.spring_security_jwt.dto.JwtAuthResponse;
import com.webmotech.spring_security_jwt.dto.SignIn;
import com.webmotech.spring_security_jwt.dto.SignUp;

public interface AuthenticationService {
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse signUp(SignUp signIn);
    String refreshToken(String token);

}
