package io.dcns.wantitauction.domain.pointLog.service;

import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogQueryRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointLogService {

    private final PointLogQueryRepository pointLogQueryRepository;

    public List<PointLogResponseDto> getPointLogs(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pointLogQueryRepository
            .findAllPointLogsPageable(user, pageable).getContent();
    }
}
