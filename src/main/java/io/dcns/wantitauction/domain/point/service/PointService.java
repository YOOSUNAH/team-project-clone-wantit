package io.dcns.wantitauction.domain.point.service;

import io.dcns.wantitauction.domain.event.WinningBidEvent;
import io.dcns.wantitauction.domain.point.dto.PointChangedResponseDto;
import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.dto.PointResponseDto;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.repository.PointRepository;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.pointLog.entity.PointLogStatus;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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
        PointLogStatus pointLogStatus = PointLogStatus.CHARGE;
        if (!checkSymbol(pointRequestDto)) {
            throw new IllegalArgumentException("포인트를 0 이상의 값으로 입력해주세요.");
        }
        Point point = pointRepository.findByUserId(user.getUserId()).orElse(null);
        // 포인트 엔티티가 없을때
        if (point == null) {
            Point newPoint = new Point(user);
            newPoint.changePoint(pointRequestDto.getChangedPoint());
            PointLog pointLog = new PointLog(newPoint, pointRequestDto, pointLogStatus);
            pointRepository.save(newPoint);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, newPoint, pointLog);
        } else { // 포인트 엔티티가 있을 때
            point.changePoint(pointRequestDto.getChangedPoint());
            PointLog pointLog = new PointLog(point, pointRequestDto, pointLogStatus);
            pointLogRepository.save(pointLog);
            return new PointChangedResponseDto(user, point, pointLog);
        }
    }

    @Transactional
    public PointChangedResponseDto withdrawPoint(User user, PointRequestDto pointRequestDto) {
        PointLogStatus pointLogStatus = PointLogStatus.WITHDRAWAL;
        if (checkSymbol(pointRequestDto)) {
            throw new IllegalArgumentException("포인트를 0 이하의 값으로 입력해주세요.");
        }
        Point point = findPoint(user.getUserId());
        checkPoint(point, pointRequestDto);
        point.changePoint(pointRequestDto.getChangedPoint());
        PointLog pointLog = new PointLog(point, pointRequestDto, pointLogStatus);
        pointLogRepository.save(pointLog);
        return new PointChangedResponseDto(user, point, pointLog);
    }

    public PointResponseDto getPoint(User user) {
        final Point point = findPoint(user.getUserId());
        return new PointResponseDto(point);
    }

    public void returnBidPoint(Long userId, Long availablePoint) {
        Point point = findPoint(userId);
        point.returnBidPoint(availablePoint);
    }

    public void subtractPoint(Long userId, Long availablePoint) {
        Point point = findPoint(userId);
        point.subtractPoint(availablePoint);
    }

    public Point findPoint(Long userId) {
        return pointRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("포인트가 존재하지 않습니다.")
        );
    }

    private void checkPoint(Point point, PointRequestDto pointRequestDto) {
        if (point.getPoint() < Math.abs(pointRequestDto.getChangedPoint())) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }
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

    @EventListener(classes = {WinningBidEvent.class})
    public void winningBidEventListenerInPoint(WinningBidEvent winningBidEvent) {
        // 포인트 정산
        Long winnerId = winningBidEvent.getWinnerId();
        Long winnerPrice = winningBidEvent.getBidPrice();
        Long auctionItemId = winningBidEvent.getAuctionItemId();

        Point point = pointRepository.findByUserId(winnerId).orElseThrow(
            () -> new IllegalArgumentException("포인트가 존재하지 않습니다.")
        );
        point.winBidSettlement(winnerPrice);

        PointLog pointLog = new PointLog((-1) * winnerPrice, PointLogStatus.SUCCESSFUL_BID, "낙찰",
            point, auctionItemId);
        pointLogRepository.save(pointLog);
    }
}
