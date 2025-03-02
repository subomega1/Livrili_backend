package com.sfar.livrili.Service;

import com.sfar.livrili.Domains.Dto.AuthDto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.User;

import java.util.UUID;

public interface UserService {

    String addUser(UserDtoRequest user);

    User getUser(UUID userId);


}
