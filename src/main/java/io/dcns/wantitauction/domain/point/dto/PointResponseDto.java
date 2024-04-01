package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import lombok.Getter;

@Getter
public class PointResponseDto {

    private final Long pointId;

    private final Long userId;

    private final Long point;

    private final Long availablePoint;

    public PointResponseDto(Point point) {
        this.pointId = point.getPointId();
        this.userId = point.getUserId();
        this.point = point.getPoint();
        this.availablePoint = point.getAvailablePoint();
    }
}
