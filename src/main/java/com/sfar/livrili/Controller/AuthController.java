package com.sfar.livrili.Controller;

import com.sfar.livrili.Domains.Dto.AuthDto.AuthResponseDto;
import com.sfar.livrili.Domains.Dto.AuthDto.LoginRequestDto;
import com.sfar.livrili.Domains.Dto.AuthDto.ModifyAuthRes;
import com.sfar.livrili.Domains.Dto.AuthDto.ModifyUserRequestDto;
import com.sfar.livrili.Domains.Dto.AuthDto.UserDtoRequest;
import com.sfar.livrili.Domains.Dto.ErrorDto.ApiErrorResponse;
import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Mapper.UserMapper;
import com.sfar.livrili.Service.AuthenticationService;
import com.sfar.livrili.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("v1/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and management") // Swagger Tag
public class AuthController {

        private final UserService userService;
        private final AuthenticationService authenticationService;
        private final UserMapper userMapper;
        private final UserDetailsService userDetailsService;

        @Operation(summary = "User Signup", description = "Registers a new user and returns a success message.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User successfully registered", content = @Content(schema = @Schema(implementation = String.class))),

                        @ApiResponse(responseCode = "422", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
        })
        @PostMapping("/signUp")
        public ResponseEntity<String> signup(@Valid @RequestBody UserDtoRequest user) {
                String res = userService.addUser(user);
                return new ResponseEntity<>(res, HttpStatus.CREATED);
        }

        @Operation(summary = "User Login", description = "Authenticates a user and returns a JWT token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
        })

        @PostMapping("/login")
        public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
                UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(),
                                loginRequest.getPassword());
                String token = authenticationService.generateToken(userDetails);
                AuthResponseDto authResponseDto = AuthResponseDto.builder()
                                .token(token)
                                .expiresIn(84600L)
                                .message("login successful")
                                .build();
                return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
        }

        @Operation(summary = "Get User Info", description = "Retrieves the authenticated user's details.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = User.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
        })
        @GetMapping
        public ResponseEntity<Object> getUser(HttpServletRequest request) {
                UUID userId = (UUID) request.getAttribute("userId");
                User user = userService.getUser(userId);
                return new ResponseEntity<>(userMapper.ToClientOrDeliveryPerson(user), HttpStatus.OK);
        }

        @Operation(summary = "Update User Info", description = "Updates the authenticated user's details.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated", content = @Content(schema = @Schema(implementation = ModifyAuthRes.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
        })
        @PutMapping()
        public ResponseEntity<ModifyAuthRes> modifyUser(@RequestBody ModifyUserRequestDto modifyUserRequest,
                        HttpServletRequest request) {
                UUID userId = (UUID) request.getAttribute("userId");
                User user = userService.modifyUser(modifyUserRequest, userId);
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                String token = authenticationService.generateToken(userDetails);
                AuthResponseDto authResponseDto = AuthResponseDto.builder()
                                .token(token)
                                .expiresIn(84600L)
                                .message("update successful")
                                .build();
                ModifyAuthRes modifyAuthRes = ModifyAuthRes.builder()
                                .authResponseDto(authResponseDto)
                                .clientOrDeliveryGuy(userMapper.ToClientOrDeliveryPerson(user))
                                .build();
                return new ResponseEntity<>(modifyAuthRes, HttpStatus.OK);
        }
}
