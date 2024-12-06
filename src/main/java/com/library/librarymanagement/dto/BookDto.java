package com.library.librarymanagement.dto;

import lombok.Builder;
import lombok.Getter;

public class BookDto {

    @Getter
    @Builder
    public static class GetResponse {
        private String id;
        private String title;
        private String author;
        private int inventory;
        private int loanedInventory;
    }

    @Getter
    public static class SaveRequest {
        private String title;
        private String author;
        private int inventory;
    }

    @Getter
    public static class UpdateRequest {
        private int inventory;
    }
}
