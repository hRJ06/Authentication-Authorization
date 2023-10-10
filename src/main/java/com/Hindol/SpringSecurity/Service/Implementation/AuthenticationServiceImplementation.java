package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Model.Role;
import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.JWTAuthenticationResponse;
import com.Hindol.SpringSecurity.Payload.SignInDTO;
import com.Hindol.SpringSecurity.Payload.SignUpDTO;
import com.Hindol.SpringSecurity.Payload.SignUpResponse;
import com.Hindol.SpringSecurity.Repository.OTPRepository;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import com.Hindol.SpringSecurity.Service.AuthenticationService;
import com.Hindol.SpringSecurity.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Hindol.SpringSecurity.Model.OTP;

import java.util.Optional;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private OTPRepository otpRepository;
    public SignUpResponse signUp(SignUpDTO signUpDTO) {
        String providedOTP = signUpDTO.getOtp();
        Optional<OTP> latestOTP = otpRepository.findLatestByEmail(signUpDTO.getEmail());
        if(latestOTP.isPresent()) {
            OTP storedOTP = latestOTP.get();
            if(providedOTP.equals(storedOTP.getOTP())) {
                Optional<User> existingUser = userRepository.findByEmail(signUpDTO.getEmail());
                if(existingUser.isPresent()) {
                    return SignUpResponse.USER_ALREADY_EXISTS;
                }
                User user = new User();
                user.setEmail(signUpDTO.getEmail());
                user.setFirstName(signUpDTO.getFirstName());
                user.setLastName(signUpDTO.getLastName());
                user.setRole(Role.USER);
                user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
                userRepository.save(user);
                return SignUpResponse.SUCCESS;
            }
            else {
                return SignUpResponse.OTP_VALIDATION_FAILED;
            }
        }
        else {
            return SignUpResponse.OTP_VALIDATION_FAILED;
        }
    }
    public JWTAuthenticationResponse signIn(SignInDTO signInDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));
        var user = userRepository.findByEmail(signInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid User Name or Password"));
        var JWT = jwtService.generateToken(user);
        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(JWT);
        return jwtAuthenticationResponse;
    }

}
