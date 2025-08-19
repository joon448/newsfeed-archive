package org.example.newsfeedproejct.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeedproejct.comment.entity.Comment;

import java.time.LocalDateTime;

public class CommentUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "내용은 필수값입니다.")
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private final Long id;
        private final Long userId;
        private final Long boardId;
        private final String content;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static Response from(Comment comment) {
            return Response.builder()
                    .id(comment.getId())
                    .userId(comment.getUser().getId())
                    .boardId(comment.getBoard().getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
}
