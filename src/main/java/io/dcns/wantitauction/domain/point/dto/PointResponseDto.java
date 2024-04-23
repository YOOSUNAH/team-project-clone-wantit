package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import lombok.Getter;

@Getter
public class PointResponseDto {

    private final Long pointId;

    private final Long userId;

    private final String nickName;

    private final Long point;

    private final Long availablePoint;

    public PointResponseDto(Point point, String nickName) {
        this.pointId = point.getPointId();
        this.userId = point.getUserId();
        this.nickName = nickName;
        this.point = point.getPoint();
        this.availablePoint = point.getAvailablePoint();
    }
}
