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
        if (!checkSymbol(pointRequestDto)) {
            throw new IllegalArgumentException("포인트를 0 이상의 값으로 입력해주세요.");
        }
        Point point = pointRepository.findByUserId(user.getUserId()).orElse(null);
        // 포인트 엔티티가 없을때
        if (point == null) {
            Point newPoint = new Point(user);
            newPoint.changePoint(pointRequestDto.getChangedPoint());
            PointLog pointLog = new PointLog(newPoint, pointRequestDto);
            pointRepository.save(newPoint);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, newPoint, pointLog);
        } else { // 포인트 엔티티가 있을 때
            point.changePoint(pointRequestDto.getChangedPoint());
            PointLog pointLog = new PointLog(point, pointRequestDto);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, point, pointLog);
        }
    }

    @Transactional
    public PointChangedResponseDto withdrawPoint(User user, PointRequestDto pointRequestDto) {
        if (checkSymbol(pointRequestDto)) {
            throw new IllegalArgumentException("포인트를 0 이하의 값으로 입력해주세요.");
        }
        Point point = findPoint(user);
        checkPoint(point, pointRequestDto);
        point.changePoint(pointRequestDto.getChangedPoint());
        PointLog pointLog = new PointLog(point, pointRequestDto);
        pointLogRepository.save(pointLog);
        return new PointChangedResponseDto(user, point, pointLog);
    }

    public PointResponseDto getPoint(User user) {
        final Point point = findPoint(user);
        return new PointResponseDto(point);
    }

    private void checkPoint(Point point, PointRequestDto pointRequestDto) {
        if (point.getPoint() < Math.abs(pointRequestDto.getChangedPoint())) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }
    }

    private Point findPoint(User user) {
        return pointRepository.findByUserId(user.getUserId()).orElseThrow(
            () -> new IllegalArgumentException("포인트가 존재하지 않습니다.")
        );
    }

    private boolean checkSymbol(PointRequestDto pointRequestDto) {
        if (pointRequestDto.getChangedPoint() < 0) {
            return false;
        } else if (pointRequestDto.getChangedPoint() > 0) {
            return true;
        } else {
            throw new IllegalArgumentException("포인트를 입력해주세요.");
        }
    }
}
