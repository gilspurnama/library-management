package com.library.librarymanagement.dto;

import lombok.*;

public class UserDto {

    @Getter
    @Builder
    public static class GetResponse {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String roleName;
    }

    @Getter
    public static class SaveRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String roleName;
    }

    @Getter
    public static class UpdateRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserCredential {
        private String fullName;
        private String userId;
        private String email;
        private String roleName;
    }

    @Getter
    @Builder
    public static class GetToken {
        private String token;
    }

    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
