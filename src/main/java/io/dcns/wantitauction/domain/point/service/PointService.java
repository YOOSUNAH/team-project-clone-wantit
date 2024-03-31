package io.dcns.wantitauction.domain.point.service;

import io.dcns.wantitauction.domain.point.dto.PointChangedResponseDto;
import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.dto.PointResponseDto;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.repository.PointRepository;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    @Transactional
    public PointChangedResponseDto putPoint(User user, PointRequestDto pointRequestDto) {
        // 포인트 엔티티가 없을때
        if (findPoint(user) == null) {
            Point point = new Point(user);
            point.putPoint(pointRequestDto.getChangedPoint());
            Long plusPoint = plusPoint(pointRequestDto);
            PointLog pointLog = new PointLog(plusPoint, point, pointRequestDto);
            pointRepository.save(point);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, point, pointLog);
        } else { // 포인트 엔티티가 있을 때
            Point point = findPoint(user);
            point.putPoint(pointRequestDto.getChangedPoint());
            Long plusPoint = plusPoint(pointRequestDto);
            PointLog pointLog = new PointLog(plusPoint, point, pointRequestDto);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, point, pointLog);
        }
    }

    public PointChangedResponseDto withdrawPoint(User user, PointRequestDto pointRequestDto) {
        Point point = findPoint(user);
        checkPoint(point, pointRequestDto);
        point.withdrawPoint(pointRequestDto.getChangedPoint());
        Long minusPoint = minusPoint(pointRequestDto);
        PointLog pointLog = new PointLog(point, pointRequestDto, minusPoint);
        pointLogRepository.save(pointLog);
        return new PointChangedResponseDto(user, point, pointLog);
    }

    public PointResponseDto getPoint(User user) {
        final Point point = findPoint(user);
        return new PointResponseDto(point);
    }

    private void checkPoint(Point point, PointRequestDto pointRequestDto) {
        if (point.getPoint() < pointRequestDto.getChangedPoint()) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }
    }

    private Point findPoint(User user) {
        return pointRepository.findByUserId(user.getUserId()).orElseThrow(
            () -> new IllegalArgumentException("포인트가 존재하지 않습니다.")
        );
    }

    private Long plusPoint(PointRequestDto pointRequestDto) {
        return +pointRequestDto.getChangedPoint();
    }

    private Long minusPoint(PointRequestDto pointRequestDto) {
        return -pointRequestDto.getChangedPoint();
    }
}
