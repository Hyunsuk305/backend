package com.example.seouldata.repository;

import com.example.seouldata.entity.Comment;
import com.example.seouldata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);

    void deleteAllByUser(User user);
}
