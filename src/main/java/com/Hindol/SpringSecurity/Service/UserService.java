package com.Hindol.SpringSecurity.Service;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.UpdateUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    User updateUserDetails(UpdateUserDTO updateUserDTO,String token);
}
