package io.dcns.wantitauction.domain.pointLog.service;

import io.dcns.wantitauction.domain.pointLog.dto.PointLogPageableResponseDto;
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

    public PointLogPageableResponseDto getPointLogs(User user, int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        List<PointLogResponseDto> pointLogResponseDtoList = pointLogQueryRepository
            .findAllPointLogsPageable(user, pageable, status).getContent();
        int totalPage = pointLogQueryRepository.findAllPointLogsPageable(user, pageable, status)
            .getTotalPages();
        return new PointLogPageableResponseDto(pointLogResponseDtoList, size, page + 1, totalPage);
    }
}
