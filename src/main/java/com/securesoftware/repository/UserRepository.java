package com.securesoftware.repository;

import com.securesoftware.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "UPDATE users u SET u.attempts = ?1 WHERE u.email = ?2", nativeQuery = true)
    @Modifying
    public void updateFailedAttempts(int failAttempts, String email);
}