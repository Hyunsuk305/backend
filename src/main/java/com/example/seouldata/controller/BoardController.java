package com.example.seouldata.controller;

import com.example.seouldata.common.ApiResponseDto;
import com.example.seouldata.common.SuccessResponseDto;
import com.example.seouldata.dto.BoardRequestDto;
import com.example.seouldata.dto.BoardResponseDto;
import com.example.seouldata.security.UserDetailsImpl;
import com.example.seouldata.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 목록 조회
    @GetMapping("/api/posts")
    public ApiResponseDto<List<BoardResponseDto>> getPosts() {
        return boardService.getPosts();
    }

    // 게시글 작성
    @PostMapping("/api/post")
    public ApiResponseDto<BoardResponseDto> createPost(@RequestBody BoardRequestDto requestsDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createPost(requestsDto, userDetails.getUser());
    }

    // 선택된 게시글 조회
    @GetMapping("/api/post/{id}")
    public ApiResponseDto<BoardResponseDto> getPost(@PathVariable Long id) {
        return boardService.getPost(id);
    }

    // 선택된 게시글 수정
    @PutMapping("/api/post/{id}")
    public ApiResponseDto<BoardResponseDto> updatePost(@PathVariable Long id, @RequestBody BoardRequestDto requestsDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updatePost(id, requestsDto, userDetails.getUser());
    }

    // 선택된 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ApiResponseDto<SuccessResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deletePost(id, userDetails.getUser());
    }

}