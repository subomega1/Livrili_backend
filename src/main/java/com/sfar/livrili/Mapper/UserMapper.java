package com.sfar.livrili.Mapper;

import com.sfar.livrili.Domains.Dto.AuthDto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.DeliveryPerson;
import com.sfar.livrili.Domains.Entities.Role;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Domains.Dto.UsersDto.ClientDto;
import com.sfar.livrili.Domains.Dto.UsersDto.DeliveryGuyDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class UserMapper {

    public Object toUser(UserDtoRequest userDto) {
        if (userDto == null) {
            log.error("userDto is null");
            throw new RuntimeException("userDto is null");
        }
        if (userDto.getRole().equals(Role.CLIENT)) {
            Client client = Client.builder()
                    .build();
            client.setEmail(userDto.getEmail());
            client.setPhone(userDto.getPhone());
            client.setPassword(userDto.getPassword());
            client.setFirstName(userDto.getFirstName());
            client.setLastName(userDto.getLastName());
            client.setRole(userDto.getRole());
            client.setGender(userDto.getGender());
            return client;
        }
        DeliveryPerson deliveryPerson = DeliveryPerson.builder()
                .rating(-1)
                .ratingCount(0)
                .build();
        deliveryPerson.setEmail(userDto.getEmail());
        deliveryPerson.setPhone(userDto.getPhone());
        deliveryPerson.setRole(userDto.getRole());
        deliveryPerson.setFirstName(userDto.getFirstName());
        deliveryPerson.setLastName(userDto.getLastName());
        deliveryPerson.setPassword(userDto.getPassword());
        deliveryPerson.setGender(userDto.getGender());

        return deliveryPerson;

    }

    public Object ToClientOrDeliveryPerson(User user) {
        if (user == null) {
            log.error("user is null");
            throw new RuntimeException("user is null");
        }
        String userPhoneString = user.getPhone();
        int length = userPhoneString.length();
        userPhoneString = "*".repeat(length) + userPhoneString.substring(length - 4);

        String userEmail = user.getEmail();
        int atIndex = userEmail.indexOf('@');
        String localPart = userEmail.substring(0, atIndex);
        String domain = userEmail.substring(atIndex);
        String maskedLocalPart = "*".repeat(localPart.length() - 3) + localPart.substring(localPart.length() - 3);
        String maskedEmail = maskedLocalPart + domain;
        if (user.getRole().equals(Role.CLIENT)) {
            return ClientDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .gender(user.getGender())
                    .role(user.getRole())
                    .phone(userPhoneString)
                    .email(maskedEmail)
                    .build();
        } else {
            return DeliveryGuyDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .gender(user.getGender())
                    .role(user.getRole())
                    .rating(((DeliveryPerson) user).getRating())
                    .rattingCount(((DeliveryPerson) user).getRatingCount())
                    .phone(userPhoneString)
                    .email(maskedEmail)
                    .build();
        }

    }

}
