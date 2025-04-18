package io.dcns.wantitauction.domain.admin.controller;

import io.dcns.wantitauction.domain.admin.dto.UserPageableResponseDto;
import io.dcns.wantitauction.domain.admin.service.AdminService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ResponseDto<UserPageableResponseDto>> getUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        UserPageableResponseDto userList = adminService.getUsers(userDetails, page, size);
        return ResponseDto.of(HttpStatus.OK, userList);
    }
}



