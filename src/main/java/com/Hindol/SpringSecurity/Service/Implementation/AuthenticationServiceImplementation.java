package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Model.Role;
import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.JWTAuthenticationResponse;
import com.Hindol.SpringSecurity.Payload.SignInDTO;
import com.Hindol.SpringSecurity.Payload.SignUpDTO;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import com.Hindol.SpringSecurity.Service.AuthenticationService;
import com.Hindol.SpringSecurity.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User signUp(SignUpDTO signUpDTO) {
        User user = new User();
        user.setEmail(signUpDTO.getEmail());
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        return userRepository.save(user);
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
