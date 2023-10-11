package com.Hindol.SpringSecurity.Payload;

public enum ResetPasswordTokenResponse {
    SUCCESS,
    USER_DOES_NOT_EXIST,
    INTERNAL_SERVER_ERROR,
    TOKEN_EXPIRED,
    INVALID_TOKEN,
}
