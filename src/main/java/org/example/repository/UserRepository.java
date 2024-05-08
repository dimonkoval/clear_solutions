package org.example.repository;

import java.time.LocalDate;
import java.util.List;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByBirthDateBetween(LocalDate from, LocalDate to);
}
