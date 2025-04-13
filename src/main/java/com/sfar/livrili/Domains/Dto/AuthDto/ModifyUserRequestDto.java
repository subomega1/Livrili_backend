package com.sfar.livrili.Domains.Dto.AuthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyUserRequestDto {

    private String oldEmail;
    private String newEmail;

    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    private String firstName;

    private String lastName;

    private String oldPhone;
    private String newPhone;
}
