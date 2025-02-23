package com.sfar.livrili.Service;

import com.sfar.livrili.Domains.Dto.UserDto;
import com.sfar.livrili.Domains.Dto.UserDtoRequest;
import com.sfar.livrili.Domains.Entities.User;

public interface UserService {

    UserDto addUser(UserDtoRequest user);


}
