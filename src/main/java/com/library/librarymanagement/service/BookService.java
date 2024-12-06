package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.BookDto;
import com.library.librarymanagement.dto.LoanDto;
import com.library.librarymanagement.dto.UserDto;

public interface BookService {

    BookDto.GetResponse getBook(String bookId);
    BookDto.GetResponse createBook(BookDto.SaveRequest saveRequest, UserDto.UserCredential userCredential);
    BookDto.GetResponse updateBook(String bookId, BookDto.UpdateRequest updateRequest, UserDto.UserCredential userCredential);
    BookDto.GetResponse deleteBook(String bookId, UserDto.UserCredential userCredential);

    LoanDto.GetResponse loanBook(String bookId, String userId, UserDto.UserCredential userCredential);
    LoanDto.GetResponse returnBook(String bookId, String userId, UserDto.UserCredential userCredential);
}
