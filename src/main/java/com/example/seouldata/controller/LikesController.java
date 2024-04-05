package com.example.seouldata.controller;

import com.example.seouldata.common.ApiResponseDto;
import com.example.seouldata.dto.BoardResponseDto;
import com.example.seouldata.dto.CommentResponseDto;
import com.example.seouldata.security.UserDetailsImpl;
import com.example.seouldata.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesService likesService;

    // 게시글 좋아요
    @PutMapping("/post/{id}")
    public ApiResponseDto<BoardResponseDto> likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.likePost(id, userDetails.getUser());
    }

    // 댓글 좋아요
    @PutMapping("/comment/{id}")
    public ApiResponseDto<CommentResponseDto> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.likeComment(id, userDetails.getUser());
    }
}
