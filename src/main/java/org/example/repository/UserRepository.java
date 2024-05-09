package org.example.repository;

import org.example.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByBirthDateBetween(LocalDate from, LocalDate to, Pageable pegeable);
}
