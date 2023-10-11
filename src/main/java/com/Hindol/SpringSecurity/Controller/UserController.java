package com.Hindol.SpringSecurity.Controller;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.UpdateUserDTO;
import com.Hindol.SpringSecurity.Service.UserService;
import jakarta.servlet.http.HttpServlet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi USER");
    }
    @PutMapping("/update-details")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserDTO updateUserDTO,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            User updatedUser = this.userService.updateUserDetails(updateUserDTO,token);
            if(updatedUser != null) {
                return ResponseEntity.ok(this.modelMapper.map(updatedUser, UpdateUserDTO.class));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Internal Server Error");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please provide a Token");
        }
    }
}
