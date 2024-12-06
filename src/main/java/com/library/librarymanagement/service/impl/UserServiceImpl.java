package com.library.librarymanagement.service.impl;

import com.library.librarymanagement.dto.UserDto;
import com.library.librarymanagement.exception.ResponseException;
import com.library.librarymanagement.model.Users;
import com.library.librarymanagement.repository.UserRepository;
import com.library.librarymanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.library.librarymanagement.util.ExceptionEnum.*;
import static com.library.librarymanagement.util.JwtUtil.JwtGenerator;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto.GetResponse createUser(UserDto.SaveRequest request) {
        if (RegexChecker(request.getEmail().toLowerCase(), "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ResponseException(EMAIL_WRONG_FORMAT.message(), EMAIL_WRONG_FORMAT.code(), EMAIL_WRONG_FORMAT.httpStatus());
        }
        if (RegexChecker(request.getPassword(), "^(?=(.*[A-Z]))[A-Za-z0-9]{1,8}$")) {
            throw new ResponseException(PASSWORD_WRONG_FORMAT.message(), PASSWORD_WRONG_FORMAT.code(), PASSWORD_WRONG_FORMAT.httpStatus());
        }
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new ResponseException(EMAIL_ALREADY_EXIST.message(), EMAIL_ALREADY_EXIST.code(), EMAIL_ALREADY_EXIST.httpStatus());
        }
        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRoleName(request.getRoleName());
        userRepository.save(user);

        return UserDto.GetResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roleName(user.getRoleName())
                .build();
    }

    @Override
    public UserDto.GetResponse getUser(String id) {
        Users extUser = userRepository.findUsersById(id).orElseThrow(() -> new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.code(), USER_NOT_FOUND.httpStatus()));

        return UserDto.GetResponse.builder()
                .id(extUser.getId())
                .firstName(extUser.getFirstName())
                .lastName(extUser.getLastName())
                .email(extUser.getEmail())
                .roleName(extUser.getRoleName())
                .build();
    }

    @Override
    public UserDto.GetResponse updateUser(UserDto.UpdateRequest request, String userId, UserDto.UserCredential userCredential) {
        Users extUser = userRepository.findUsersById(userId).orElseThrow(() -> new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.code(), USER_NOT_FOUND.httpStatus()));
        if (RoleChecker(extUser, userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        if (RegexChecker(request.getEmail().toLowerCase(), "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ResponseException(EMAIL_WRONG_FORMAT.message(), EMAIL_WRONG_FORMAT.code(), EMAIL_WRONG_FORMAT.httpStatus());
        }

        extUser.setFirstName(request.getFirstName());
        extUser.setLastName(request.getLastName());
        extUser.setEmail(request.getEmail());
        extUser.setRoleName(request.getRoleName());
        userRepository.save(extUser);

        return UserDto.GetResponse.builder()
                .id(extUser.getId())
                .firstName(extUser.getFirstName())
                .lastName(extUser.getLastName())
                .email(extUser.getEmail())
                .roleName(extUser.getRoleName())
                .build();
    }

    @Override
    public UserDto.GetResponse deleteUser(String id, UserDto.UserCredential userCredential) {
        Users extUser = userRepository.findUsersById(id).orElseThrow(() -> new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.code(), USER_NOT_FOUND.httpStatus()));
        if (RoleChecker(extUser, userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        userRepository.delete(extUser);
        return UserDto.GetResponse.builder()
                .id(extUser.getId())
                .firstName(extUser.getFirstName())
                .lastName(extUser.getLastName())
                .email(extUser.getEmail())
                .build();
    }
    @Override
    public UserDto.GetToken loginUser(UserDto.LoginRequest loginRequest) {
        Users users = userRepository.findUsersByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()).orElseThrow(() -> new ResponseException(WRONG_CREDENTIAL.message(), WRONG_CREDENTIAL.code(), WRONG_CREDENTIAL.httpStatus()));
        String fullName = users.getFirstName() + " " + users.getLastName();
        return UserDto.GetToken.builder()
                .token(JwtGenerator(UserDto.UserCredential.builder()
                        .userId(users.getId())
                        .email(users.getEmail())
                        .roleName(users.getRoleName())
                        .fullName(fullName)
                        .build()))
                .build();
    }

    boolean RegexChecker(String stringCheck, String regexPattern) {
        return !stringCheck.matches(regexPattern);
    }

    boolean RoleChecker(Users extUser, UserDto.UserCredential userCredential) {
        String fullName = extUser.getFirstName() + " " + extUser.getLastName();
        boolean check = Objects.equals(extUser.getId(), userCredential.getUserId()) && Objects.equals(extUser.getEmail(), userCredential.getEmail()) && Objects.equals(fullName, userCredential.getFullName());
        return !check;
    }
}
