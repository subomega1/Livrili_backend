package com.sfar.livrili.Domains.Dto.AuthDto;

import com.sfar.livrili.Domains.Entities.Gender;
import com.sfar.livrili.Domains.Entities.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRequest {

    private String email;

    private String password;

    private String confirmPassword;

    private String firstName;

    private String lastName;

    private String phone;

    private Gender gender;

    private Role role;
}
