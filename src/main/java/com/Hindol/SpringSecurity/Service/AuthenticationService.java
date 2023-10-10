package com.Hindol.SpringSecurity.Service;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.JWTAuthenticationResponse;
import com.Hindol.SpringSecurity.Payload.SignInDTO;
import com.Hindol.SpringSecurity.Payload.SignUpDTO;
import com.Hindol.SpringSecurity.Payload.SignUpResponse;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpDTO signUpDTO);
    JWTAuthenticationResponse signIn(SignInDTO signInDTO);
}
