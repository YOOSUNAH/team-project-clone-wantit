package io.dcns.wantitauction.domain.user.entity;

import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
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

    public User(String email, String password, String username, String nickname,
        String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void update(String nickname, String phoneNumber, String address) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void updatePassword(String password) {
        if (password.equals(this.password)) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }
        this.password = password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
