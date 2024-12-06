package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.UserDto;

public interface UserService {

    UserDto.GetResponse getUser(String userId);
    UserDto.GetResponse createUser(UserDto.SaveRequest saveRequest);
    UserDto.GetResponse updateUser(UserDto.UpdateRequest updateRequest, String userId, UserDto.UserCredential userCredential);
    UserDto.GetResponse deleteUser(String userId, UserDto.UserCredential userCredential);
    UserDto.GetToken loginUser(UserDto.LoginRequest loginRequest);
}
