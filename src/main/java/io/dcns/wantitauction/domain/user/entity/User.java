package io.dcns.wantitauction.domain.user.entity;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@SQLDelete(sql = "update users set deleted_at = NOW() where user_id = ?")
@SQLRestriction(value = "deleted_at is NULL")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private LocalDateTime deletedAt;

    public User(SignupRequestDto signupRequestDto) {
        this.email = signupRequestDto.getEmail();
        this.password = signupRequestDto.getPassword();
        this.username = signupRequestDto.getUsername();
        this.nickname = signupRequestDto.getNickname();
        this.phoneNumber = signupRequestDto.getPhoneNumber();
        this.address = signupRequestDto.getAddress();
    }

//    public static User of(String email, String password) {
//        return new User(email, password, null);
//    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
        this.phoneNumber = userRequestDto.getPhoneNumber();
        this.address = userRequestDto.getAddress();
    }

    public void updatePassword(String password) {
        if (password.equals(this.password)) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }
        this.password = password;
    }
}
