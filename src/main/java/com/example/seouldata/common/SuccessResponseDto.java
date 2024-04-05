package com.example.seouldata.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseDto {
    private int status;
    private String message;

    @Builder
    private SuccessResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static SuccessResponseDto of(HttpStatus status, String message) {
        return SuccessResponseDto.builder()
                .status(status.value())
                .message(message)
                .build();
    }
}