package io.dcns.wantitauction.domain.admin.service;

import io.dcns.wantitauction.domain.admin.dto.UserPageableResponseDto;
import io.dcns.wantitauction.domain.admin.dto.UsersResponseDto;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserQueryRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserQueryRepository userQueryRepository;

    public UserPageableResponseDto getUsers(UserDetailsImpl userDetails, int page, int size) {

        UserRoleEnum role = userDetails.getUser().getRole();
        if (role != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<UsersResponseDto> responseDtoPage = userQueryRepository.findAll(pageable);
        int totalPage = responseDtoPage.getTotalPages();

        return new UserPageableResponseDto(
            responseDtoPage.getContent(), size, page + 1, totalPage);
    }
}


