package io.dcns.wantitauction.domain.admin.service;

import io.dcns.wantitauction.domain.admin.dto.UsersResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UsersResponseDto> getUsers(UserDetailsImpl userDetails) {

        UserRoleEnum role  =  userDetails.getUser().getRole();
        if (role != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다.");
        }
        List<User> userList = userRepository.findAll();

        return userList.stream().map(UsersResponseDto::new).toList();
    }
}


