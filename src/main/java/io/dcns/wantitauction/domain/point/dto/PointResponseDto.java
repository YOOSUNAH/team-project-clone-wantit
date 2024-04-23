package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.user.entity.User;
import lombok.Getter;

@Getter
public class PointResponseDto {

    private final Long pointId;

    private final Long userId;

    private final String nickName;

    private final String username;

    private final Long point;

    private final Long availablePoint;

    public PointResponseDto(Point point, User user) {
        this.pointId = point.getPointId();
        this.userId = point.getUserId();
        this.nickName = user.getNickname();
        this.username = user.getUsername();
        this.point = point.getPoint();
        this.availablePoint = point.getAvailablePoint();
    }
}
