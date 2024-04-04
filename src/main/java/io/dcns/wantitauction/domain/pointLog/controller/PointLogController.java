package io.dcns.wantitauction.domain.pointLog.controller;

import io.dcns.wantitauction.domain.pointLog.dto.PointLogResponseDto;
import io.dcns.wantitauction.domain.pointLog.service.PointLogService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointLogController {

    private final PointLogService pointLogService;

    @GetMapping("/v1/points/log")
    public ResponseEntity<ResponseDto<List<PointLogResponseDto>>> getPointLogs(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable
    ) {
        List<PointLogResponseDto> pointLogResponseDtoList = pointLogService
            .getPointLogs(userDetails.getUser(), pageable);

        return ResponseDto.of(HttpStatus.OK, pointLogResponseDtoList);
    }
}
