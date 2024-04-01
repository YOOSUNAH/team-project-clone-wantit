package io.dcns.wantitauction.global.impl;

import io.dcns.wantitauction.domain.user.entity.User;
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

    public UserDetails getUser(Long userId) throws UsernameNotFoundException {
        User user = new User();
        user.setUserId(userId);
        return new UserDetailsImpl(user);
    }
}
