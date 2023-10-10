package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Model.OTP;
import com.Hindol.SpringSecurity.Repository.OTPRepository;
import com.Hindol.SpringSecurity.Service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPServiceImplementation implements OTPService {
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;
    @Override
    public void createOTP(String email) {
        String OTP = generateOTP();
        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setOTP(OTP);
        this.otpRepository.save(otp);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setText("OTP - " + OTP);
            simpleMailMessage.setSubject("OTP Verification For Registration");
            javaMailSender.send(simpleMailMessage);
        }
        catch (Exception e) {
            return;
        }
    }
}
