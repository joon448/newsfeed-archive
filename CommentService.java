package org.example.newsfeedproejct.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeedproejct.board.entity.Board;
import org.example.newsfeedproejct.board.repository.BoardRepository;
import org.example.newsfeedproejct.comment.dto.CommentCreateDto;
import org.example.newsfeedproejct.comment.dto.CommentSearchDto;
import org.example.newsfeedproejct.comment.dto.CommentUpdateDto;
import org.example.newsfeedproejct.comment.entity.Comment;
import org.example.newsfeedproejct.comment.repository.CommentRepository;
import org.example.newsfeedproejct.global.exception.GlobalException;
import org.example.newsfeedproejct.global.exception.errorcode.CommentErrorCode;
import org.example.newsfeedproejct.user.entity.User;
import org.example.newsfeedproejct.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateDto.Response createComment(Long userId, Long boardId, String content) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        Comment comment = new Comment(user, board, content);

        commentRepository.save(comment);
        return CommentCreateDto.Response.from(comment);
    }
/*
    @Transactional(readOnly = true)
    public CommentSearchDto.Response searchBoardWithComments(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        List<CommentSearchDto> comments = commentRepository.findByBoardIdOrderByModifiedAtDesc(boardId) // 최신 수정일 기준 내림차순 정렬
                .stream()
                .map(CommentSearchDto.Response::from)
                .toList();

        return BoardWithCommentsDto.Response.of(BoardGetDto.Response.from(board), comments);
    }
*/
    @Transactional(readOnly = true)
    public CommentSearchDto.Response findById(Long boardId, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if(!comment.getBoard().getId().equals(boardId)){
            throw new GlobalException(CommentErrorCode.COMMENT_BOARD_MISMATCH);
        }
        return CommentSearchDto.Response.from(comment);
    }

    @Transactional
    public CommentUpdateDto.Response updateComment(Long userId, Long boardId, Long commentId, String content) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if(!comment.getBoard().getId().equals(boardId)){
            throw new GlobalException(CommentErrorCode.COMMENT_BOARD_MISMATCH);
        }
        if(!comment.getUser().getId().equals(userId)){
            throw new GlobalException(CommentErrorCode.COMMENT_NOT_OWNER);
        }
        comment.updateContent(content);
        commentRepository.flush();
        return CommentUpdateDto.Response.from(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long boardId, Long commentId){
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if(!comment.getBoard().getId().equals(boardId)){
            throw new GlobalException(CommentErrorCode.COMMENT_BOARD_MISMATCH);
        }
        if(!comment.getUser().getId().equals(userId)){
            throw new GlobalException(CommentErrorCode.COMMENT_NOT_OWNER);
        }
        commentRepository.delete(comment);
    }

}
