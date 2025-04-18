package io.dcns.wantitauction.domain.pointLog.repository;

import static io.dcns.wantitauction.domain.point.entity.QPoint.point1;
import static io.dcns.wantitauction.domain.pointLog.entity.QPointLog.pointLog;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.pointLog.entity.PointLogStatus;
import io.dcns.wantitauction.domain.user.entity.User;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointLogQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<PointLogResponseDto> findAllPointLogsPageable(
            User user,
            Pageable pageable,
            String status) {

        Long totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(pointLog)
                .leftJoin(pointLog.point, point1)
                .where(point1.userId.eq(user.getUserId()),
                        pointLogStatusEq(status))
                .fetch().get(0);

        List<PointLogResponseDto> pointLogs = jpaQueryFactory
                .select(Projections.fields(PointLogResponseDto.class,
                        point1.userId,
                        pointLog.pointLogId,
                        pointLog.changedPoint,
                        pointLog.status,
                        pointLog.details,
                        pointLog.auctionItemId,
                        pointLog.createdAt,
                        pointLog.updatedAt))
                .from(pointLog)
                .leftJoin(pointLog.point, point1)
                .where(point1.userId.eq(user.getUserId()),
                        pointLogStatusEq(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(pointLog.createdAt.desc())
                .fetch();

        return PageableExecutionUtils.getPage(pointLogs, pageable, () -> totalSize);
    }

    private BooleanExpression pointLogStatusEq(String status) {
        if (status != null) {
            PointLogStatus pointLogStatus = PointLogStatus.valueOf(status);
            return pointLog.status.eq(pointLogStatus);
        }
        return null;
    }
}
