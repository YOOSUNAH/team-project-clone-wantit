package io.dcns.wantitauction.global.impl;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetails getUserDetails(Claims info) {
//        User user = new User();
//        //info 에서 정보를 추출하여 User 생성
//        user.setUserId(info.getSubject());
//
//        return new UserDetailsImpl(user);
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
