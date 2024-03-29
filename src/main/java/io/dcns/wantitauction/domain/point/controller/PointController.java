package io.dcns.wantitauction.domain.point.controller;

import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.dto.PointResponseDto;
import io.dcns.wantitauction.domain.point.service.PointService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/v1/points")
    public ResponseEntity<ResponseDto<PointResponseDto>> putPoint(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PointRequestDto pointRequestDto
    ) {
        PointResponseDto pointRecordResponseDto = pointService
            .putPoint(userDetails.getUser(), pointRequestDto);

        return ResponseDto.of(HttpStatus.OK, pointRecordResponseDto);
    }
}
