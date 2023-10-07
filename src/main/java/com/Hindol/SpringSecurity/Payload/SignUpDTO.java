package com.Hindol.SpringSecurity.Payload;

import lombok.Data;

@Data
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
