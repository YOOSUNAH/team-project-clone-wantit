package io.dcns.wantitauction.domain.point.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.dcns.wantitauction.domain.point.dto.PointChangedResponseDto;
import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.dto.PointResponseDto;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.repository.PointRepository;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.pointLog.repository.PointLogRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("포인트 서비스 유닛 테스트")
class PointServiceTest {

    @InjectMocks
    PointService pointService;

    @Mock
    PointRepository pointRepository;

    @Mock
    PointLogRepository pointLogRepository;

    @Mock
    UserService userService;

    private User user;

    @BeforeEach
    void setUser() {
        user = User.builder()
            .userId(1L)
            .build();
    }

    @DisplayName("포인트 적립 테스트_성공")
    @Test
    void putPoint() {
        // given
        PointRequestDto pointRequestDto = new PointRequestDto(1000L, "적립");
        given(pointRepository.findByUserId(user.getUserId())).willReturn(Optional.empty());
        given(pointLogRepository.save(any(PointLog.class))).willAnswer(
            invocation -> invocation.<PointLog>getArgument(0));

        // when
        PointChangedResponseDto pointChangedResponseDto = pointService.putPoint(user,
            pointRequestDto);

        // then
        assertEquals(pointRequestDto.getChangedPoint(), pointChangedResponseDto.getChangedPoint());
        assertEquals(pointRequestDto.getDetails(), pointChangedResponseDto.getDetails());
        verify(pointRepository, times(1)).findByUserId(user.getUserId());
        verify(pointLogRepository, times(1)).save(any(PointLog.class));
    }

    @DisplayName("포인트 적립 테스트_기존 포인트 존재")
    @Test
    void putPoint_ExistingPoint() {
        // given
        Point existingPoint = new Point(user);
        existingPoint.changePoint(500L);
        PointRequestDto pointRequestDto = new PointRequestDto(1000L, "적립");
        given(pointRepository.findByUserId(user.getUserId())).willReturn(
            Optional.of(existingPoint));
        given(pointLogRepository.save(any(PointLog.class))).willAnswer(
            invocation -> invocation.<PointLog>getArgument(0));

        // when
        PointChangedResponseDto pointChangedResponseDto = pointService.putPoint(user,
            pointRequestDto);

        // then
        assertEquals(1500L, existingPoint.getPoint());
        assertEquals(pointRequestDto.getDetails(), pointChangedResponseDto.getDetails());
        verify(pointRepository, times(1)).findByUserId(user.getUserId());
        verify(pointLogRepository, times(1)).save(any(PointLog.class));
    }

    @DisplayName("포인트 적립 테스트_0 이하의 값 입력")
    @Test
    void putPoint_NegativePoint() {
        // given
        PointRequestDto pointRequestDto = new PointRequestDto(-1000L, "적립");

        // when
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                pointService.putPoint(user, pointRequestDto);
            });

        // then
        assertEquals("포인트를 0 이상의 값으로 입력해주세요.", exception.getMessage());
    }

    @DisplayName("포인트 출금 테스트_성공")
    @Test
    void withdrawPoint() {
        // given
        Point point = new Point(user);
        point.changePoint(2000L);
        PointRequestDto pointRequestDto = new PointRequestDto(-1000L, "출금");
        given(pointRepository.findByUserId(any(Long.class))).willReturn(Optional.of(point));
        given(pointLogRepository.save(any(PointLog.class))).willAnswer(
            invocation -> invocation.<PointLog>getArgument(0));
        given(userService.findByUserId(any(Long.class))).willReturn(user);

        // when
        PointChangedResponseDto pointChangedResponseDto = pointService.withdrawPoint(user,
            pointRequestDto);

        // then
        assertEquals(1000L, point.getPoint());
        assertEquals(pointRequestDto.getDetails(), pointChangedResponseDto.getDetails());
        verify(pointRepository, times(1)).findByUserId(user.getUserId());
        verify(pointLogRepository, times(1)).save(any(PointLog.class));
    }

    @DisplayName("포인트 출금 테스트_0 이상의 값 입력")
    @Test
    void withdrawPoint_PositivePoint() {
        // given
        PointRequestDto pointRequestDto = new PointRequestDto(1000L, "출금");

        // when
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                pointService.withdrawPoint(user, pointRequestDto);
            });

        // then
        assertEquals("포인트를 0 이하의 값으로 입력해주세요.", exception.getMessage());
    }

    @DisplayName("포인트 조회 테스트_성공")
    @Test
    void getPoint() {
        // given
        Point point = new Point(user);
        point.changePoint(2000L);
        given(pointRepository.findByUserId(user.getUserId())).willReturn(Optional.of(point));
        given(userService.findByUserId(any(Long.class))).willReturn(user);

        // when
        PointResponseDto pointResponseDto = pointService.getPoint(user);

        // then
        assertEquals(point.getPoint(), pointResponseDto.getPoint());
        verify(pointRepository, times(1)).findByUserId(user.getUserId());
    }
}

