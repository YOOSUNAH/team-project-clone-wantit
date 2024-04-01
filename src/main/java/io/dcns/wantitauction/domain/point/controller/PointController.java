package io.dcns.wantitauction.domain.point.controller;

import io.dcns.wantitauction.domain.point.dto.PointChangedResponseDto;
import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.dto.PointResponseDto;
import io.dcns.wantitauction.domain.point.service.PointService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/points")
public class PointController {

    private final PointService pointService;

    @PostMapping
    public ResponseEntity<ResponseDto<PointChangedResponseDto>> putPoint(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PointRequestDto pointRequestDto
    ) {
        PointChangedResponseDto pointRecordResponseDto = pointService
            .putPoint(userDetails.getUser(), pointRequestDto);

        return ResponseDto.of(HttpStatus.OK, pointRecordResponseDto);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<ResponseDto<PointChangedResponseDto>> withdrawPoint(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PointRequestDto pointRequestDto
    ) {
        PointChangedResponseDto pointChangedResponseDto = pointService
            .withdrawPoint(userDetails.getUser(), pointRequestDto);

        return ResponseDto.of(HttpStatus.OK, pointChangedResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PointResponseDto>> getPoint(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PointResponseDto pointResponseDto = pointService
            .getPoint(userDetails.getUser());

        return ResponseDto.of(HttpStatus.OK, pointResponseDto);
    }
}
