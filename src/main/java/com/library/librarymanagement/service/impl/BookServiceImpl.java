package com.library.librarymanagement.service.impl;

import com.library.librarymanagement.dto.BookDto;
import com.library.librarymanagement.dto.LoanDto;
import com.library.librarymanagement.dto.UserDto;
import com.library.librarymanagement.exception.ResponseException;
import com.library.librarymanagement.model.Books;
import com.library.librarymanagement.model.Loans;
import com.library.librarymanagement.model.Users;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.LoanRepository;
import com.library.librarymanagement.repository.UserRepository;
import com.library.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.library.librarymanagement.util.ExceptionEnum.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    @Override
    public BookDto.GetResponse getBook(String bookId) {
        Books book = bookRepository.findBookById(bookId).orElseThrow(() -> new ResponseException(BOOK_NOT_FOUND.message(), BOOK_NOT_FOUND.code(), BOOK_NOT_FOUND.httpStatus()));
        return BookDto.GetResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .inventory(book.getInventory())
                .build();
    }

    @Override
    public BookDto.GetResponse createBook(BookDto.SaveRequest request, UserDto.UserCredential userCredential) {
        if (!adminCheck(userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        if(bookRepository.existsBooksByTitleAndAuthor(request.getTitle(), request.getAuthor())) {
            throw new ResponseException(BOOK_ALREADY_EXIST.message(), BOOK_ALREADY_EXIST.code(), BOOK_ALREADY_EXIST.httpStatus());
        }
        Books book = new Books();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setInventory(request.getInventory());
        bookRepository.save(book);
        return BookDto.GetResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .inventory(book.getInventory())
                .build();
    }

    @Override
    public BookDto.GetResponse updateBook(String bookId, BookDto.UpdateRequest request, UserDto.UserCredential userCredential) {
        if (!adminCheck(userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        Books extBook = bookRepository.findBookById(bookId).orElseThrow(() -> new ResponseException(BOOK_NOT_FOUND.message(), BOOK_NOT_FOUND.code(), BOOK_NOT_FOUND.httpStatus()));
        extBook.setInventory(request.getInventory());
        bookRepository.save(extBook);

        return BookDto.GetResponse.builder()
                .id(extBook.getId())
                .title(extBook.getTitle())
                .author(extBook.getAuthor())
                .inventory(extBook.getInventory())
                .build();
    }

    @Override
    public BookDto.GetResponse deleteBook(String bookId, UserDto.UserCredential userCredential) {
        if (!adminCheck(userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        Books extBook = bookRepository.findBookById(bookId).orElseThrow(() -> new ResponseException(BOOK_NOT_FOUND.message(), BOOK_NOT_FOUND.code(), BOOK_NOT_FOUND.httpStatus()));
        bookRepository.delete(extBook);
        return BookDto.GetResponse.builder()
                .id(extBook.getId())
                .title(extBook.getTitle())
                .author(extBook.getAuthor())
                .inventory(extBook.getInventory())
                .build();
    }

    @Override
    public LoanDto.GetResponse loanBook(String bookId, String userId, UserDto.UserCredential userCredential) {
        Books book = bookRepository.findBookById(bookId).orElseThrow(() -> new ResponseException(BOOK_NOT_FOUND.message(), BOOK_NOT_FOUND.code(), BOOK_NOT_FOUND.httpStatus()));
        if (book.getInventory() == book.getLoanedInventory()) {
            throw new ResponseException(BOOK_INSUFFICIENT.message(), BOOK_INSUFFICIENT.code(), BOOK_INSUFFICIENT.httpStatus());
        }
        Users user = userRepository.findUsersById(userId).orElseThrow(() -> new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.code(), USER_NOT_FOUND.httpStatus()));
        if (!adminCheck(userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        Loans extLoan = loanRepository.findLoanByUserIdAndBookId(userId, bookId);
        if (extLoan != null) {
            throw new ResponseException(USER_BORROW_LIMIT.message(), USER_BORROW_LIMIT.code(), USER_BORROW_LIMIT.httpStatus());
        }
        LocalDateTime now = LocalDateTime.now();
        Loans loan = new Loans();
        loan.setBookId(bookId);
        loan.setUserId(userId);
        loan.setLoanDate(now);
        loanRepository.save(loan);

        book.setLoanedInventory(book.getLoanedInventory() + 1);
        bookRepository.save(book);

        return LoanDto.GetResponse.builder()
                .userId(userId)
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .bookId(bookId)
                .title(book.getTitle())
                .author(book.getAuthor())
                .loanDate(now)
                .returnDate(null)
                .build();
    }

    @Override
    public LoanDto.GetResponse returnBook(String bookId, String userId, UserDto.UserCredential userCredential) {
        if (!adminCheck(userCredential)) {
            throw new ResponseException(UNAUTHORIZED.message(), UNAUTHORIZED.code(), UNAUTHORIZED.httpStatus());
        }
        Books book = bookRepository.findBookById(bookId).orElseThrow(() -> new ResponseException(BOOK_NOT_FOUND.message(), BOOK_NOT_FOUND.code(), BOOK_NOT_FOUND.httpStatus()));
        Users user = userRepository.findUsersById(userId).orElseThrow(() -> new ResponseException(USER_NOT_FOUND.message(), USER_NOT_FOUND.code(), USER_NOT_FOUND.httpStatus()));
        Loans extLoan = loanRepository.findLoanByUserIdAndBookId(userId, bookId);
        if (extLoan == null) {
            throw new ResponseException(LOAN_NOT_FOUND.message(), LOAN_NOT_FOUND.code(), LOAN_NOT_FOUND.httpStatus());
        }
        LocalDateTime now = LocalDateTime.now();
        extLoan.setReturnDate(now);
        loanRepository.save(extLoan);

        book.setLoanedInventory(book.getLoanedInventory() - 1);
        bookRepository.save(book);

        return LoanDto.GetResponse.builder()
                .userId(userId)
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .bookId(bookId)
                .title(book.getTitle())
                .author(book.getAuthor())
                .loanDate(extLoan.getLoanDate())
                .returnDate(now)
                .build();
    }

    Boolean adminCheck(UserDto.UserCredential userCredential) {
        if (!Objects.equals(userCredential.getRoleName(), "admin")){
            return false;
        }
        return userRepository.existsByEmail(userCredential.getEmail());
    }
}
