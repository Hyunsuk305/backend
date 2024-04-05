package com.example.seouldata.controller;

import com.example.seouldata.common.ApiResponseDto;
import com.example.seouldata.common.SuccessResponseDto;
import com.example.seouldata.dto.CommentRequestDto;
import com.example.seouldata.dto.CommentResponseDto;
import com.example.seouldata.security.UserDetailsImpl;
import com.example.seouldata.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment/{id}")   // 여기서 ID는 게시글의 id
    public ApiResponseDto<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id, requestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")    // 여기서 ID는 댓글의 id
    public ApiResponseDto<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")     // 여기서 ID는 댓글의 id
    public ApiResponseDto<SuccessResponseDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

}