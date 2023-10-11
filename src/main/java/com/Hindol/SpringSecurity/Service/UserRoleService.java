package com.Hindol.SpringSecurity.Service;

import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Payload.PasswordChangeDTO;
import com.Hindol.SpringSecurity.Payload.UpdateUserDTO;

public interface UserRoleService {
    User updateUserDetails(UpdateUserDTO updateUserDTO, String token);
    User changePassword(PasswordChangeDTO passwordChangeDTO, String token);
}
