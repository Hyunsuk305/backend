package com.example.seouldata.service;

import com.example.seouldata.common.ApiResponseDto;
import com.example.seouldata.common.ResponseUtil;
import com.example.seouldata.common.SuccessResponseDto;
import com.example.seouldata.dto.BoardRequestDto;
import com.example.seouldata.dto.BoardResponseDto;
import com.example.seouldata.dto.CommentResponseDto;
import com.example.seouldata.entity.Board;
import com.example.seouldata.entity.Comment;
import com.example.seouldata.entity.User;
import com.example.seouldata.entity.enumSet.ErrorType;
import com.example.seouldata.entity.enumSet.UserRoleEnum;
import com.example.seouldata.exception.RestApiException;
import com.example.seouldata.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Configuration
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 전체 목록 조회
    @Bean
    @Transactional(readOnly = true)
    public ApiResponseDto<List<BoardResponseDto>> getPosts() {

        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            // 댓글리스트 작성일자 기준 내림차순 정렬
            board.getCommentList()
                    .sort(Comparator.comparing(Comment::getModifiedAt)
                            .reversed());

            // 대댓글은 제외 부분 작성
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : board.getCommentList()) {
                if (comment.getParentCommentId() == null) {
                    commentList.add(CommentResponseDto.from(comment));
                }
            }

            // List<BoardResponseDto> 로 만들기 위해 board 를 BoardResponseDto 로 만들고, list 에 dto 를 하나씩 넣는다.
            responseDtoList.add(BoardResponseDto.from(board, commentList));
        }

        return ResponseUtil.ok(responseDtoList);

    }

    // 게시글 작성
    @Bean
    @Transactional
    public ApiResponseDto<BoardResponseDto> createPost(BoardRequestDto requestsDto, User user) {

        // 작성 글 저장
        Board board = boardRepository.save(Board.of(requestsDto, user));

        // BoardResponseDto 로 변환 후 responseEntity body 에 담아 반환
        return ResponseUtil.ok(BoardResponseDto.from(board));

    }

    // 선택된 게시글 조회
    @Bean
    @Transactional(readOnly = true)
    public ApiResponseDto<BoardResponseDto> getPost(Long id) {
        // Id에 해당하는 게시글이 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) { // 해당 게시글이 없다면
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 댓글리스트 작성일자 기준 내림차순 정렬
        board.get()
                .getCommentList()
                .sort(Comparator.comparing(Comment::getModifiedAt)
                        .reversed());

        // 대댓글은 제외 부분 작성
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : board.get().getCommentList()) {
            if (comment.getParentCommentId() == null) {
                commentList.add(CommentResponseDto.from(comment));
            }
        }

        // board 를 responseDto 로 변환 후, ResponseEntity body 에 dto 담아 리턴
        return ResponseUtil.ok(BoardResponseDto.from(board.get(), commentList));
    }

    // 선택된 게시글 수정
    @Bean
    @Transactional
    public ApiResponseDto<BoardResponseDto> updatePost(Long id, BoardRequestDto requestsDto, User user) {

        // 선택한 게시글이 DB에 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 게시글 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) { // 일치하는 게시물이 없다면
            throw new RestApiException(ErrorType.NOT_WRITER);
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 수정
        board.get().update(requestsDto, user);
        boardRepository.flush(); // responseDto 에 modifiedAt 업데이트 해주기 위해 flush 사용

        return ResponseUtil.ok(BoardResponseDto.from(board.get()));

    }

    // 게시글 삭제
    @Bean
    @Transactional
    public ApiResponseDto<SuccessResponseDto> deletePost(Long id, User user) {

        // 선택한 게시글이 DB에 있는지 확인
        Optional<Board> found = boardRepository.findById(id);
        if (found.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (삭제하려는 사용자가 관리자라면 게시글 삭제 가능)
        Optional<Board> board = boardRepository.findByIdAndUser(id, user);
        if (board.isEmpty() && user.getRole() == UserRoleEnum.USER) { // 일치하는 게시물이 없다면
            throw new RestApiException(ErrorType.NOT_WRITER);
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 수정
        boardRepository.deleteById(id);
        return ResponseUtil.ok(SuccessResponseDto.of(HttpStatus.OK, "게시글 삭제 성공"));

    }

}