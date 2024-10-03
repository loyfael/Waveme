package com.alpineblue.waveme_backend.crud.repository;

import com.alpineblue.waveme_backend.crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
