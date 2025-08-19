package org.example.newsfeedproejct.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeedproejct.board.entity.Board;
import org.example.newsfeedproejct.global.entity.SoftDeletableEntity;
import org.example.newsfeedproejct.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;

    public Comment(User user, Board board, String content){
        this.user = user;
        this.board = board;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
