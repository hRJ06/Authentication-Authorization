package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Configuration.CONSTANT;
import com.Hindol.SpringSecurity.Model.Role;
import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.*;
import com.Hindol.SpringSecurity.Repository.OTPRepository;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import com.Hindol.SpringSecurity.Service.AuthenticationService;
import com.Hindol.SpringSecurity.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Hindol.SpringSecurity.Model.OTP;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
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
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;
    private static final String ALLOWED_CHARACTERS = CONSTANT.UIDCharacter;
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

    @Override
    public ResetPasswordTokenResponse resetPasswordToken(ResetPasswordTokenRequest resetPasswordTokenRequest) {
        String email = resetPasswordTokenRequest.getEmail();
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            String token = generateRandomUUID();
            User existingUser = user.get();
            existingUser.setResetPasswordToken(token);
            Instant now = Instant.now();
            Instant oneMinuteLater = now.plusSeconds(60);
            existingUser.setResetPasswordTokenExpires(Timestamp.from(oneMinuteLater));
            this.userRepository.save(existingUser);
            try {
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(sender);
                simpleMailMessage.setTo(email);
                String url = "http://localhost:3000/reset-password/" + token;
                simpleMailMessage.setText("Your RESET PASSWORD LINK - " + url);
                simpleMailMessage.setSubject("RESET PASSWORD LINK");
                javaMailSender.send(simpleMailMessage);
            }
            catch (Exception e) {
                System.out.println(e);
                return ResetPasswordTokenResponse.INTERNAL_SERVER_ERROR;
            }
            return ResetPasswordTokenResponse.SUCCESS;
        }
        else {
            return ResetPasswordTokenResponse.USER_DOES_NOT_EXIST;
        }
    }

    @Override
    public ResetPasswordTokenResponse resetPassword(ResetPasswordTokenRequest resetPasswordTokenRequest, String token) {
        Optional<User> user = this.userRepository.findByResetPasswordTokenAndEmail(token, resetPasswordTokenRequest.getEmail());
        if(user.isPresent()) {
            User existingUser = user.get();
            Instant currentTime = Instant.now();
            Instant tokenExpirationTime = existingUser.getResetPasswordTokenExpires().toInstant();
            if(currentTime.isBefore(tokenExpirationTime)) {
                String newPassword = resetPasswordTokenRequest.getPassword();
                existingUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(existingUser);
                return ResetPasswordTokenResponse.SUCCESS;
            }
            else {
                return ResetPasswordTokenResponse.TOKEN_EXPIRED;
            }
        }
        else {
            return ResetPasswordTokenResponse.INVALID_TOKEN;
        }
    }
    private String generateRandomUUID() {
        SecureRandom random = new SecureRandom();
        StringBuilder randomString = new StringBuilder(16);
        for(int i = 0; i<16; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char token = ALLOWED_CHARACTERS.charAt(randomIndex);
            randomString.append(token);
        }
        return randomString.toString();
    }
}
