package com.example.seouldata.repository;

import com.example.seouldata.entity.Board;
import com.example.seouldata.entity.Comment;
import com.example.seouldata.entity.Likes;
import com.example.seouldata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByBoardAndUser(Board board, User user);
    Optional<Likes> findByCommentAndUser(Comment comment, User user);

    void deleteAllByUser(User user);
}
