package com.Hindol.SpringSecurity.Controller;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.*;
import com.Hindol.SpringSecurity.Service.AuthenticationService;
import com.Hindol.SpringSecurity.Service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private OTPService otpService;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        SignUpResponse response  = this.authenticationService.signUp(signUpDTO);
        if(response.equals(SignUpResponse.SUCCESS)) {
            return ResponseEntity.ok("User Registered Successfully");
        }
        else if(response.equals(SignUpResponse.OTP_VALIDATION_FAILED)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP Validation Failed");
        }
        else if(response.equals(SignUpResponse.USER_ALREADY_EXISTS)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal Error");
        }
    }
    @PostMapping("/signIn")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInDTO signInDTO) {
        return ResponseEntity.ok(authenticationService.signIn(signInDTO));
    }

    @PostMapping("/generateOTP")
    public ResponseEntity<?> generateOTP(@RequestBody OTPRequestDTO otpRequestDTO) {
        this.otpService.createOTP(otpRequestDTO.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }
}

