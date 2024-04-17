package io.dcns.wantitauction.domain.pointLog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.pointLog.dto.PointLogPageableResponseDto;
import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogQueryRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("포인트 로그 서비스 유닛 테스트")
@ExtendWith(MockitoExtension.class)
class PointLogServiceTest {

    @InjectMocks
    PointLogService pointLogService;

    @Mock
    PointLogQueryRepository pointLogQueryRepository;

    private User user;
    private List<PointLogResponseDto> pointLogResponseDtoList;
    private Page<PointLogResponseDto> pointLogResponseDtoPage;

    @BeforeEach
    void setTest() {
        user = User.builder()
            .userId(1L)
            .build();

        pointLogResponseDtoList = Collections.singletonList(new PointLogResponseDto());
        pointLogResponseDtoPage = new PageImpl<>(pointLogResponseDtoList);
    }

    @Test
    void getPointLogs() {
        // given
        int page = 0;
        int size = 10;
        String status = "CHARGE";

        // when
        when(pointLogQueryRepository.findAllPointLogsPageable(any(User.class), any(Pageable.class),
            any(String.class)))
            .thenReturn(pointLogResponseDtoPage);

        PointLogPageableResponseDto pointLogPageableResponseDto = pointLogService.getPointLogs(user,
            page, size, status);

        // then
        assertEquals(pointLogResponseDtoList,
            pointLogPageableResponseDto.getPointLogResponseDtoList());
        assertEquals(size, pointLogPageableResponseDto.getPageSize());
        assertEquals(page + 1, pointLogPageableResponseDto.getCurrentPage());
        assertEquals(pointLogResponseDtoPage.getTotalPages(),
            pointLogPageableResponseDto.getTotalPage());
    }
}
