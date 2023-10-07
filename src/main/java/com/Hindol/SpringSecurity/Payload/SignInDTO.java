package com.Hindol.SpringSecurity.Payload;

import lombok.Data;

@Data
public class SignInDTO {
    private String email;
    private String password;
}
