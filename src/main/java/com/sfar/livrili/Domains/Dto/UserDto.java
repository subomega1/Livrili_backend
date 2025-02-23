package com.sfar.livrili.Domains.Dto;

import com.sfar.livrili.Domains.Entities.Gender;
import com.sfar.livrili.Domains.Entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {

    private String email;


    private String firstName;


    private String lastName;


    private Gender gender;

    private Role role;


    private  float rating;
}
