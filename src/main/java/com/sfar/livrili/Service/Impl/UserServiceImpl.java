package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Dto.ErrorDto.FieldsError;
import com.sfar.livrili.Domains.Dto.ErrorDto.IllegalArgs;
import com.sfar.livrili.Domains.Dto.AuthDto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.Client;
import com.sfar.livrili.Domains.Entities.DeliveryPerson;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Mapper.UserMapper;
import com.sfar.livrili.Repositories.ClientRepository;
import com.sfar.livrili.Repositories.DeliveryPersonRepository;
import com.sfar.livrili.Repositories.UserRepository;
import com.sfar.livrili.Service.UserService;
import com.sfar.livrili.Validation.UserCreationValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final UserMapper userMapper;

    @Override
    public String addUser(UserDtoRequest user) {

        List<FieldsError> checkForErrors = ValidateUserRequest(user);
        if (!checkForErrors.isEmpty()) {
            System.out.println(checkForErrors);
            throw new IllegalArgs("user cannot be registered", checkForErrors);
        }
        Object userToCheck = userMapper.toUser(user);
        if (userToCheck instanceof Client) {
            ((Client) userToCheck).setPassword(passwordEncoder.encode(user.getPassword()));
            clientRepository.save((Client) userToCheck);

        } else {
            ((DeliveryPerson) userToCheck).setPassword(passwordEncoder.encode(user.getPassword()));
            deliveryPersonRepository.save((DeliveryPerson) userToCheck);
        }
        return "Your account has been created";

    }

    @Override
    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

    }

    private List<FieldsError> ValidateUserRequest(UserDtoRequest user) {
        List<FieldsError> errors = new ArrayList<>();
        if (!UserCreationValidation.notEmpty(user.getEmail())) {
            errors.add(new FieldsError("email", "Email is required"));
        } else if (!UserCreationValidation.isEmailValid(user.getEmail())) {
            errors.add(new FieldsError("email", "Invalid email"));

        }
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.add(new FieldsError("email", "Email already exist"));
        } else if (!UserCreationValidation.notEmpty(user.getPhone())) {
            errors.add(new FieldsError("phone", "Phone number is required"));
        }
        if (!UserCreationValidation.isLengthPhoneValid(user.getPhone())) {
            errors.add(new FieldsError("phone", "Invalid phone number"));
        } else if (userRepository.existsByPhone(user.getPhone())) {
            errors.add(new FieldsError("phone", "Phone number already exist"));
        } else if (!UserCreationValidation.isPhoneNumberIsValid(user.getPhone())) {
            errors.add(new FieldsError("phone", "Phone number must contains only digits"));
        }
        if (!UserCreationValidation.notEmpty(user.getPassword())) {
            errors.add(new FieldsError("password", "Password is required"));
        } else if (!UserCreationValidation.passwordRespect(user.getPassword())) {
            errors.add(new FieldsError("password", "Password must be at least 8 characters"));
        }
        if (!UserCreationValidation.notEmpty(user.getConfirmPassword())) {
            errors.add(new FieldsError("confirmPassword", "Confirmed Password is required"));
        } else if (!UserCreationValidation.passwordMatch(user.getPassword(), user.getConfirmPassword())) {

            errors.add(new FieldsError("password", "Passwords do not match"));
        }
        if (!UserCreationValidation.notEmpty(user.getFirstName())) {
            errors.add(new FieldsError("firstName", "First name is required"));
        } else if (!UserCreationValidation.validateNameFields(user.getFirstName())) {
            errors.add(new FieldsError("firstName", "Invalid first name"));
        }
        if (!UserCreationValidation.notEmpty(user.getLastName())) {
            errors.add(new FieldsError("lastName", "Last name is required"));
        } else if (!UserCreationValidation.validateNameFields(user.getLastName())) {
            errors.add(new FieldsError("lastName", "Invalid last name"));
        }
        if (!UserCreationValidation.genderValid(user.getGender())) {
            errors.add(new FieldsError("gender", "Gender is required"));
        }
        if (!UserCreationValidation.roleValidation(user.getRole())) {
            errors.add(new FieldsError("role", "Role must is required"));
        }
        return errors;
    }

}
