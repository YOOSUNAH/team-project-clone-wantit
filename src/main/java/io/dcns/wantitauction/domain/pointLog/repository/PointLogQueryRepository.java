package io.dcns.wantitauction.domain.pointLog.repository;

import static io.dcns.wantitauction.domain.point.entity.QPoint.point1;
import static io.dcns.wantitauction.domain.pointLog.entity.QPointLog.pointLog;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointLogQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<PointLogResponseDto> findAllPointLogs(User user) {
        return jpaQueryFactory
            .select(Projections.fields(PointLogResponseDto.class,
                point1.userId,
                pointLog.pointLogId,
                pointLog.changedPoint,
                pointLog.details,
                pointLog.changedDate))
            .from(pointLog)
            .leftJoin(pointLog.point, point1)
            .where(point1.userId.eq(user.getUserId()))
            .fetch();
    }
}
