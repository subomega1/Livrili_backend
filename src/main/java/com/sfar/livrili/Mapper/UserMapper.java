package com.sfar.livrili.Mapper;


import com.sfar.livrili.Domains.Dto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.DeliveryPerson;
import com.sfar.livrili.Domains.Entities.Role;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class UserMapper {

public  Object  toUser(UserDtoRequest userDto) {
    if(userDto == null) {
        throw new RuntimeException("userDto is null");
    }
    if (userDto.getRole().equals(Role.CLIENT)){
        Client client = Client.builder()
                .address(userDto.getAddress())
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


}
