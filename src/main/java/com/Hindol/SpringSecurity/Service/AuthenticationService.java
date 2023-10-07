package com.Hindol.SpringSecurity.Service;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.JWTAuthenticationResponse;
import com.Hindol.SpringSecurity.Payload.SignInDTO;
import com.Hindol.SpringSecurity.Payload.SignUpDTO;

public interface AuthenticationService {
    User signUp(SignUpDTO signUpDTO);
    JWTAuthenticationResponse signIn(SignInDTO signInDTO);
}
