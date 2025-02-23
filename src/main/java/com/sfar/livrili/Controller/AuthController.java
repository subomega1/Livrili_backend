package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.AuthResponseDto;
import com.sfar.livrili.Domains.Dto.LoginRequestDto;
import com.sfar.livrili.Domains.Dto.UserDto;
import com.sfar.livrili.Domains.Dto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Service.AuthenticationService;
import com.sfar.livrili.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDtoRequest user) {
       UserDto userDto= userService.addUser(user);
       return new ResponseEntity<>(userDto, HttpStatus.CREATED);



    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login( @RequestBody LoginRequestDto loginRequest) {
       UserDetails userDetails =  authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
       String token = authenticationService.generateToken(userDetails);
       AuthResponseDto authResponseDto =AuthResponseDto.builder()
                       .token(token)
               .expiresIn(84600L)
               .role(userDetails.getAuthorities().stream().findFirst().get().getAuthority())
               .build();
       return new ResponseEntity<>(authResponseDto, HttpStatus.OK);

    }



}
