package com.webmotech.spring_security_jwt.controller;


import com.webmotech.spring_security_jwt.config.EmailSender;
import com.webmotech.spring_security_jwt.dto.JwtAuthResponse;
import com.webmotech.spring_security_jwt.dto.SignIn;
import com.webmotech.spring_security_jwt.dto.SignUp;
import com.webmotech.spring_security_jwt.service.AuthenticationService;
import com.webmotech.spring_security_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailSender emailSender;

    private final AuthenticationService authenticationService;


    @GetMapping("/health")
    public String health() {
        return "User Controller is working";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUp signUp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            JwtAuthResponse jwtAuthResponse = authenticationService.signUp(signUp);
            return ResponseEntity.ok(jwtAuthResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User Already Exists or Invalid Data \n Exception : " + e.getMessage());
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignIn signIn, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();

        }
        try {

            ResponseEntity<JwtAuthResponse> ok = ResponseEntity.ok(authenticationService.signIn(signIn));

            emailSender.sendHtmlMail("Login Alert", ok.getBody().getUserDTO().getEmail());
            return ok;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Data \n Exception : " + e.getMessage());
        }
    }


}
