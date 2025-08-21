package org.example.newsfeedproejct.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.newsfeedproejct.board.entity.Board;
import org.example.newsfeedproejct.board.repository.BoardRepository;
import org.example.newsfeedproejct.comment.entity.Comment;
import org.example.newsfeedproejct.comment.repository.CommentRepository;
import org.example.newsfeedproejct.user.entity.User;
import org.example.newsfeedproejct.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommentNPlusOneTest {
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    EntityManager em;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    void enableStats() {
        var sf = emf.unwrap(org.hibernate.SessionFactory.class);
        sf.getStatistics().setStatisticsEnabled(true);
        sf.getStatistics().clear();
    }

    private long afterCount() {
        return emf.unwrap(org.hibernate.SessionFactory.class).getStatistics().getPrepareStatementCount();
    }

    @Test
    void nPlusOne_비교_EntityGraph() {
        // 0) 시드
        User u1 = userRepository.save(new User("alice", "a@a.com", "Password11!!}"));
        Board b  = boardRepository.save(new Board("subj","content", u1));
        commentRepository.save(new Comment(u1, b, "hi"));
        commentRepository.save(new Comment(u1, b, "hello"));

        Long boardId = b.getId();

        // ★ 핵심: 1차 캐시 비우기 (N+1 가림 방지)
        em.flush();
        em.clear();

        var stats = emf.unwrap(org.hibernate.SessionFactory.class).getStatistics();

        // -------- No EntityGraph (엔티티그래프 없는 메서드) --------
        stats.clear();
        var noEg = commentRepository.findAllByBoardIdAndDeletedAtIsNullOrderByCreatedAt(boardId);
        long beforeNoEg = afterCount();

        // LAZY 강제 초기화: id 말고 닉네임/제목 등 비-식별자 접근
        noEg.forEach(c -> {
            c.getUser().getNickname();
            c.getBoard().getSubject();
        });
        long afterNoEg  = afterCount();
        System.out.println("[No EG] 목록후=" + beforeNoEg + ", 연관접근후=" + afterNoEg + " (증가=" + (afterNoEg - beforeNoEg) + ")");

        // -------- With EntityGraph (엔티티그래프 있는 메서드) --------
        stats.clear();
        var withEg = commentRepository.findByBoardIdAndDeletedAtIsNullOrderByCreatedAt(boardId);
        long beforeEg = afterCount();

        withEg.forEach(c -> {
            c.getUser().getNickname();
            c.getBoard().getSubject();
        });
        long afterEg  = afterCount();
        System.out.println("[With EG] 목록후=" + beforeEg + ", 연관접근후=" + afterEg + " (증가=" + (afterEg - beforeEg) + ")");

        // (선택) 기대치 검증
        // assertTrue(afterEg - beforeEg <= 1);
        // assertTrue(afterNoEg - beforeNoEg >= withEg.size()); // 대략적 N+1 징후
    }
}
