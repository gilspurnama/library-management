package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loans, String> {

    @Query("select l from Loans l where l.userId = ?1 and l.bookId = ?2 and l.returnDate is null")
    Loans findLoanByUserIdAndBookId(String userId, String bookId);
}
