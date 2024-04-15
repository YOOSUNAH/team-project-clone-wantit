package io.dcns.wantitauction.domain.admin.controller;

import io.dcns.wantitauction.domain.admin.dto.UsersResponseDto;
import io.dcns.wantitauction.domain.admin.service.AdminService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ResponseDto<List<UsersResponseDto>>> getUsers(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<UsersResponseDto>  userList =  adminService.getUsers(userDetails);
        return ResponseDto.of(HttpStatus.OK, userList);
    }
}



