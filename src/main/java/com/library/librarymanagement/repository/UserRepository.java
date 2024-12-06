package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findUsersById(String userId);
    Boolean existsByEmail(String email);
    Optional<Users> findUsersByEmailAndPassword(String email, String password);
}
