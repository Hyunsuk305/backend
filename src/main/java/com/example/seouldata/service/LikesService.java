package com.example.seouldata.service;

import com.example.seouldata.common.ApiResponseDto;
import com.example.seouldata.common.ResponseUtil;
import com.example.seouldata.dto.BoardResponseDto;
import com.example.seouldata.dto.CommentResponseDto;
import com.example.seouldata.entity.Board;
import com.example.seouldata.entity.Comment;
import com.example.seouldata.entity.Likes;
import com.example.seouldata.entity.User;
import com.example.seouldata.entity.enumSet.ErrorType;
import com.example.seouldata.exception.RestApiException;
import com.example.seouldata.repository.BoardRepository;
import com.example.seouldata.repository.CommentRepository;
import com.example.seouldata.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 게시글 좋아요 기능
    public ApiResponseDto<BoardResponseDto> likePost(Long id, User user) {
        // 선택한 게시글이 DB에 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 이전에 좋아요 누른 적 있는지 확인
        Optional<Likes> found = likesRepository.findByBoardAndUser(board.get(), user);
        if (found.isEmpty()) {  // 좋아요 누른적 없음
            Likes likes = Likes.of(board.get(), user);
            likesRepository.save(likes);
        } else { // 좋아요 누른 적 있음
            likesRepository.delete(found.get()); // 좋아요 눌렀던 정보를 지운다.
            likesRepository.flush();
        }

        return ResponseUtil.ok(BoardResponseDto.from(board.get()));
    }

    // 댓글 좋아요 기능
    public ApiResponseDto<CommentResponseDto> likeComment(Long id, User user) {
        // 선택한 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 이전에 좋아요 누른 적 있는지 확인
        Optional<Likes> found = likesRepository.findByCommentAndUser(comment.get(), user);
        if (found.isEmpty()) {  // 좋아요 누른적 없음
            Likes likes = Likes.of(comment.get(), user);
            likesRepository.save(likes);
        } else { // 좋아요 누른 적 있음
            likesRepository.delete(found.get()); // 좋아요 눌렀던 정보를 지운다.
            likesRepository.flush();
        }

        return ResponseUtil.ok(CommentResponseDto.from(comment.get()));
    }
}