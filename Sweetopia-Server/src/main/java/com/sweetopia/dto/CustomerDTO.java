package com.sweetopia.dto;

import com.sweetopia.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String userName;
    private String userPassword;
    private String email;
    private Role role;
}
