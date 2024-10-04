package com.alpineblue.waveme_backend.crud.repository;

import com.alpineblue.waveme_backend.crud.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
