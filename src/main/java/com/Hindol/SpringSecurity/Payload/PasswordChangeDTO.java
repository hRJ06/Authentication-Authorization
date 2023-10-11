package com.Hindol.SpringSecurity.Payload;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String password;
    private String confirmPassword;
}
