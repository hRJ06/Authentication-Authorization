package com.Hindol.SpringSecurity.Payload;

import lombok.Data;

@Data
public class ResetPasswordTokenRequest {
    private String email;
    private String password;
}
