package com.library.librarymanagement.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class LoanDto {

    @Getter
    @Builder
    public static class GetResponse {
        private String userId;
        private String fullName;
        private String email;
        private String bookId;
        private String title;
        private String author;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
    }
}
