package io.dcns.wantitauction.domain.admin.service;

import io.dcns.wantitauction.domain.admin.dto.UsersResponseDto;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserQueryRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserQueryRepository userQueryRepository;

    public List<UsersResponseDto> getUsers(UserDetailsImpl userDetails, int page, int size) {
        UserRoleEnum role = userDetails.getUser().getRole();
        if (role != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return userQueryRepository.getUserListWithPage(pageRequest.getOffset(),
                pageRequest.getPageSize())
            .stream().map(UsersResponseDto::new).toList();

    }
}


