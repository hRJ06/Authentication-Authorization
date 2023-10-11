package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.PasswordChangeDTO;
import com.Hindol.SpringSecurity.Payload.UpdateUserDTO;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import com.Hindol.SpringSecurity.Service.JWTService;
import com.Hindol.SpringSecurity.Service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImplementation implements UserRoleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User updateUserDetails(UpdateUserDTO updateUserDTO, String token) {
        String email = this.jwtService.extractUserName(token);
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            User existingUser = user.get();
            existingUser.setFirstName(updateUserDTO.getFirstName());
            existingUser.setLastName(updateUserDTO.getLastName());
            this.userRepository.save(existingUser);
            return existingUser;
        }
        else {
            return null;
        }
    }

    @Override
    public User changePassword(PasswordChangeDTO passwordChangeDTO, String token) {
        String email = this.jwtService.extractUserName(token);
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            User existingUser = user.get();
            if(passwordChangeDTO.getPassword().equals(passwordChangeDTO.getConfirmPassword())) {
                existingUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getPassword()));
                this.userRepository.save(existingUser);
                return existingUser;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
