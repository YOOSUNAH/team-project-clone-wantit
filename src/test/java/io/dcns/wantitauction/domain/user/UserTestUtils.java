package io.dcns.wantitauction.domain.user;

import io.dcns.wantitauction.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.SerializationUtils;


public class UserTestUtils {

    public static User get(User user) {return get(user, 1L);}

    public static User get(User user, Long userId){

        User newUser = SerializationUtils.clone(user);

        ReflectionTestUtils.setField(newUser, User.class, "userId", userId, Long.class);

        return newUser;
    }
}
