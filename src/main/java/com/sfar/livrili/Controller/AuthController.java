package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.AuthDto.AuthResponseDto;
import com.sfar.livrili.Domains.Dto.AuthDto.LoginRequestDto;
import com.sfar.livrili.Domains.Dto.AuthDto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Mapper.UserMapper;
import com.sfar.livrili.Service.AuthenticationService;
import com.sfar.livrili.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/signUp")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDtoRequest user) {
    String  res = userService.addUser(user);
       return new ResponseEntity<>(res, HttpStatus.CREATED);



    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login( @RequestBody LoginRequestDto loginRequest) {
       UserDetails userDetails =  authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
       String token = authenticationService.generateToken(userDetails);
       AuthResponseDto authResponseDto =AuthResponseDto.builder()
                       .token(token)
                        .expiresIn(84600L)
                        .message("w")
               .build();
       return new ResponseEntity<>(authResponseDto, HttpStatus.OK);

    }


    @PostMapping("/testAuth")
    public ResponseEntity<String> testAuth() {

        return ResponseEntity.ok("Hello World");
    }

    @GetMapping
    public ResponseEntity<Object> getUser( HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user=userService.getUser(userId);
        return new  ResponseEntity<>(userMapper.ToClientOrDeliveryPerson(user),HttpStatus.OK);
    }




}
