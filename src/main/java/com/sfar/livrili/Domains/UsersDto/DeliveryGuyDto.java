package com.sfar.livrili.Domains.UsersDto;

import com.sfar.livrili.Domains.Entities.Gender;
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
}
