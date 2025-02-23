package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.UserDto;
import com.sfar.livrili.Domains.Dto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDtoRequest user) {
       UserDto userDto= userService.addUser(user);
       return new ResponseEntity<>(userDto, HttpStatus.CREATED);



    }



}
