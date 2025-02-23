package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Dto.UserDto;
import com.sfar.livrili.Domains.Dto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.DeliveryPerson;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.DeliveryPersonRepository;
import com.sfar.livrili.Repositories.UserRepository;
import com.sfar.livrili.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    @Override
    public UserDto addUser( @Valid UserDtoRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already Exist");
        }
        System.out.println(user.getRole().name());

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirmed Password do not match");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole().name().equals("CLIENT")) {
            if (user.getAddress() == null) {
                throw new IllegalArgumentException("User Address is required");
            }

            Client newClient = Client.builder()
                    .address(user.getAddress())
                    .build();

            newClient.setEmail(user.getEmail());
            newClient.setPassword(user.getPassword());
            newClient.setGender(user.getGender());
            newClient.setRole(user.getRole());
            newClient.setPhone(user.getPhone());
            newClient.setFirstName(user.getFirstName());
            newClient.setLastName(user.getLastName());
            Client client = clientRepository.save(newClient);
            return convertToDto(client);
        }
        else if (user.getRole().name().equals(
                "DELIVERY_PERSON")) {
            DeliveryPerson userToSave = DeliveryPerson.builder()
                    .rating(-1F)
                    .build();
            userToSave.setEmail(user.getEmail());
            userToSave.setPassword(user.getPassword());
            userToSave.setRole(user.getRole());
            userToSave.setPhone(user.getPhone());
            userToSave.setFirstName(user.getFirstName());
            userToSave.setLastName(user.getLastName());
            userToSave.setGender(user.getGender());
            DeliveryPerson deliveryPerson = deliveryPersonRepository.save(userToSave);
            return convertToDto(deliveryPerson);
        }
        else {
            throw new IllegalArgumentException("Role not supported");
        }
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }}

