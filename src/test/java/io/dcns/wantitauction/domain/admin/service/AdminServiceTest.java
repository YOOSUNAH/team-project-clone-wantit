package io.dcns.wantitauction.domain.admin.service;

import static io.dcns.wantitauction.domain.user.UserCommonTest.EXPECTED_TOTAL_PAGE;
import static io.dcns.wantitauction.domain.user.UserCommonTest.PAGE;
import static io.dcns.wantitauction.domain.user.UserCommonTest.SIZE;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_ADDRESS;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_DETAILS;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_EMAIL;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_ID;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_NAME;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_NICKNAME;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_PASSWORD;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_PHONENUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.admin.dto.AdminResponseDto;
import io.dcns.wantitauction.domain.admin.dto.UserPageableResponseDto;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserQueryRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    AdminService adminService;

    @Mock
    UserQueryRepository userQueryRepository;

    @DisplayName("회원 전체 조회")
    @Test
    void getUsers() {
        // given
        UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
        testUserDetails.getUser().setRole(UserRoleEnum.ADMIN);

        List<AdminResponseDto> adminResponseDtoList = Collections.singletonList(new AdminResponseDto
            (
                TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_PASSWORD,
                TEST_USER_NAME, TEST_USER_NICKNAME, TEST_USER_PHONENUMBER,
                TEST_USER_ADDRESS, TEST_USER_ID
            ));
        Page<AdminResponseDto> usersResponseDtoPage = new PageImpl<>(adminResponseDtoList);

        when(userQueryRepository.findAll(any(Pageable.class)))
            .thenReturn(usersResponseDtoPage);

        // when
        UserPageableResponseDto result = adminService.getUsers(testUserDetails, PAGE, SIZE);

        // then
        assertEquals(adminResponseDtoList, result.responseDtoList());
        assertEquals(SIZE, result.pageSize());
        assertEquals(PAGE + 1, result.currentPage());
        assertEquals(EXPECTED_TOTAL_PAGE, result.totalPage());
    }
}
