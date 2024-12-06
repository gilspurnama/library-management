package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.ResponseDto;
import com.library.librarymanagement.dto.UserDto;
import com.library.librarymanagement.service.UserService;
import com.library.librarymanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto.GetResponse>> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(new ResponseDto<>(userService.getUser(userId)));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<UserDto.GetResponse>> createUser(@RequestBody UserDto.SaveRequest request) {
        return ResponseEntity.ok(new ResponseDto<>(userService.createUser(request)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto.GetResponse>> updateUser(@PathVariable String userId, @RequestBody UserDto.UpdateRequest updateRequest, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(userService.updateUser(updateRequest, userId, userCredential)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto.GetResponse>> deleteUser(@PathVariable String userId, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(userService.deleteUser(userId, userCredential)));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserDto.GetToken>> login(@RequestBody UserDto.LoginRequest request) {
        return ResponseEntity.ok(new ResponseDto<>(userService.loginUser(request)));
    }
}
