package io.dcns.wantitauction.global.impl;

import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails getUserDetails(Long userId, UserRoleEnum role)
        throws UsernameNotFoundException {
        User user = new User();
        user.setForUserDetails(userId, role);
        return new UserDetailsImpl(user);
    }
}
