package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.BookDto;
import com.library.librarymanagement.dto.LoanDto;
import com.library.librarymanagement.dto.ResponseDto;
import com.library.librarymanagement.dto.UserDto;
import com.library.librarymanagement.service.BookService;
import com.library.librarymanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<ResponseDto<BookDto.GetResponse>> getBookById(@PathVariable String bookId) {
        return ResponseEntity.ok(new ResponseDto<>(bookService.getBook(bookId)));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<BookDto.GetResponse>> createBook(@RequestBody BookDto.SaveRequest saveRequest, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(bookService.createBook(saveRequest, userCredential)));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ResponseDto<BookDto.GetResponse>> updateBook(@PathVariable String bookId, @RequestBody BookDto.UpdateRequest updateRequest, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(bookService.updateBook(bookId, updateRequest, userCredential)));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ResponseDto<BookDto.GetResponse>> deleteBook(@PathVariable String bookId, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(bookService.deleteBook(bookId, userCredential)));
    }

    @PutMapping("/{bookId}/users/{userId}/loan")
    public ResponseEntity<ResponseDto<LoanDto.GetResponse>> loan(@PathVariable String bookId, @PathVariable String userId, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(bookService.loanBook(bookId, userId, userCredential)));
    }

    @PutMapping("/{bookId}/users/{userId}/return")
    public ResponseEntity<ResponseDto<LoanDto.GetResponse>> returnBook (@PathVariable String bookId, @PathVariable String userId, HttpServletRequest request) {
        UserDto.UserCredential userCredential = JwtUtil.getCredentialJwt(request);
        return ResponseEntity.ok(new ResponseDto<>(bookService.returnBook(bookId, userId, userCredential)));
    }
}
