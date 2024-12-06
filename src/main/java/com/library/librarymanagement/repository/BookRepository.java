package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Books, String> {

    Optional<Books> findBookById(String id);
    Boolean existsBooksByTitleAndAuthor(String title, String author);
}
