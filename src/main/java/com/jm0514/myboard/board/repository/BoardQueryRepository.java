package com.jm0514.myboard.board.repository;

import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.comment.dto.CommentResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jm0514.myboard.board.domain.QBoard.board;
import static com.jm0514.myboard.comment.domain.QComment.comment;
import static com.jm0514.myboard.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<BoardTotalInfoResponse> findBoardTotalInfo(final Long lastBoardId, final int size) {
        List<BoardTotalInfoResponse> boards = findBoards(lastBoardId, size);
        List<Long> boardIds = boards.stream()
                .map(BoardTotalInfoResponse::getBoardId)
                .toList();
        Map<Long, List<CommentResponse>> commentsMap = findComments(boardIds).stream()
                .collect(Collectors.groupingBy(CommentResponse::getBoardId));
        boards.forEach(b -> b.setComments(commentsMap.getOrDefault(b.getBoardId(), new ArrayList<>())));

        return boards;
    }

    public List<BoardTotalInfoResponse> findBoards(final Long lastBoardId, final int size) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (lastBoardId != null) {
            whereClause.and(board.id.lt(lastBoardId));
        }

        return jpaQueryFactory.select(
                        Projections.constructor(
                                BoardTotalInfoResponse.class,
                                board.id,
                                board.title.value,
                                board.content.value,
                                board.member.name,
                                board.createdAt,
                                board.totalLikeCount
                        )
                )
                .from(board)
                .join(board.member, member)
                .where(whereClause)
                .orderBy(board.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentResponse> findComments(final List<Long> boardIds) {
        return jpaQueryFactory.select(
                        Projections.constructor(CommentResponse.class,
                                comment.board.id,
                                comment.commentContent.value,
                                comment.member.name,
                                comment.createdAt
                        )
                )
                .from(comment)
                .join(comment.board, board)
                .join(comment.member, member)
                .where(comment.board.id.in(boardIds))
                .fetch();
    }
}