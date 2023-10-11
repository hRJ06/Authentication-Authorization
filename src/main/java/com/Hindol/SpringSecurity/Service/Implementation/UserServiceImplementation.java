package com.Hindol.SpringSecurity.Service.Implementation;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.UpdateUserDTO;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import com.Hindol.SpringSecurity.Service.JWTService;
import com.Hindol.SpringSecurity.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };
    }

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
}
