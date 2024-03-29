package io.dcns.wantitauction.domain.pointLog.service;

import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.service.PointService;
import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogQueryRepository;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointLogService {

    private final PointLogQueryRepository pointLogQueryRepository;

    public List<PointLogResponseDto> getPointLogs(User user) {
        return pointLogQueryRepository.findAllPointLogs(user);
    }
}
