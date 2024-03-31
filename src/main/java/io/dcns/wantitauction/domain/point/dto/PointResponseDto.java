package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import lombok.Getter;

@Getter
public class PointResponseDto {

    private Long pointId;

    private Long userId;

    private Long point;

    private Long availablePoint;

    public PointResponseDto(Point point) {
        this.pointId = point.getPointId();
        this.userId = point.getUserId();
        this.point = point.getPoint();
        this.availablePoint = point.getAvailablePoint();
    }
}
