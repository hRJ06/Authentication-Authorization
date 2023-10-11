package com.Hindol.SpringSecurity.Service;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.*;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpDTO signUpDTO);
    JWTAuthenticationResponse signIn(SignInDTO signInDTO);
    ResetPasswordTokenResponse resetPasswordToken(ResetPasswordTokenRequest resetPasswordTokenRequest);
    ResetPasswordTokenResponse resetPassword(ResetPasswordTokenRequest resetPasswordTokenRequest,String token);
}
