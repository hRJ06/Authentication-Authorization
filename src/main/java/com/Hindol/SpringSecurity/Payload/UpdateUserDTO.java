package com.Hindol.SpringSecurity.Payload;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
}
