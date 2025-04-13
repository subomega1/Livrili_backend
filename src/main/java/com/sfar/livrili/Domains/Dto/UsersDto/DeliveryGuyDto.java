package com.sfar.livrili.Domains.Dto.UsersDto;

import com.sfar.livrili.Domains.Entities.Gender;
import com.sfar.livrili.Domains.Entities.Role;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryGuyDto {
    private String firstName;
    private String lastName;
    private Gender gender;
    private float rating;
    private int rattingCount;
    private Role role;
    private String phone;
    private String email;
}
