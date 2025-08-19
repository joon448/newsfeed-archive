package org.example.newsfeedproejct.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    // 4xx: 클라이언트 오류
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "삭제되었거나 존재하지 않는 댓글입니다."),
    COMMENT_BOARD_MISMATCH(HttpStatus.BAD_REQUEST, "COMMENT-002", "댓글이 요청한 게시글에 속하지 않습니다."),
    COMMENT_NOT_OWNER(HttpStatus.FORBIDDEN, "COMMENT-003", "권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
