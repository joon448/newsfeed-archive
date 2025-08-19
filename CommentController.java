package org.example.newsfeedproejct.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeedproejct.comment.dto.CommentCreateDto;
import org.example.newsfeedproejct.comment.dto.CommentSearchDto;
import org.example.newsfeedproejct.comment.dto.CommentUpdateDto;
import org.example.newsfeedproejct.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // TODO: LOGIN_USER 상수로 변경
    @PostMapping("/boards/{boardId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentCreateDto.Response createComment(@SessionAttribute("LOGIN_USER") Long userId, @PathVariable("boardId") Long boardId, @RequestBody CommentCreateDto.Request commentCreateDto) {
        return commentService.createComment(userId, boardId, commentCreateDto.getContent());
    }
    
    // TODO: 기능 구현
    /*
    @GetMapping("/boards/{boardId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentSearchDto.Response searchComments(@PathVariable("boardId") Long boardId){
        return commentService.searchComments(boardId);
    }
    */

    @GetMapping("/boards/{boardId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentSearchDto.Response findById(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId){
        return commentService.findById(boardId, commentId);
    }

    // TODO: LOGIN_USER 상수로 변경
    @PatchMapping("/boards/{boardId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentUpdateDto.Response updateComment(@SessionAttribute("LOGIN_USER") Long userId, @PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @RequestBody CommentUpdateDto.Request commentUpdateDto) {
        return commentService.updateComment(userId, boardId, commentId, commentUpdateDto.getContent());
    }

    // TODO: LOGIN_USER 상수로 변경
    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@SessionAttribute("LOGIN_USER") Long userId, @PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userId, boardId, commentId);
    }

}
