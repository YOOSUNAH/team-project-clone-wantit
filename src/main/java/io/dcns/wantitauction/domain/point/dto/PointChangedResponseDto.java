package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.pointLog.entity.PointLogStatus;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PointChangedResponseDto {

    private final Long pointId;

    private final Long pointLogId;

    private final Long userId;

    private final Long changedPoint;

    private final PointLogStatus status;

    private final String details;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;


    public PointChangedResponseDto(User user, Point point, PointLog pointLog) {
        this.pointId = point.getPointId();
        this.userId = user.getUserId();
        this.pointLogId = pointLog.getPointLogId();
        this.changedPoint = pointLog.getChangedPoint();
        this.createdAt = pointLog.getCreatedAt();
        this.updatedAt = pointLog.getUpdatedAt();
        this.details = pointLog.getDetails();
        this.status = pointLog.getStatus();
    }
}
