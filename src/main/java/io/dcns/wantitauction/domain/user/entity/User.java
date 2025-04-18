package io.dcns.wantitauction.domain.user.entity;

import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update users set deleted_at = NOW() where user_id = ?")
@SQLRestriction(value = "deleted_at is NULL")
public class User extends Timestamped implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private String nickname;

    private String phoneNumber;

    private String address;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User
            (String email,
             String password,
             String username,
             String nickname,
             String phoneNumber,
             String address,
             UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public void update(
            String nickname,
            String phoneNumber,
            String address) {
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

    public void setForUserDetails(
            Long userId,
            UserRoleEnum role) {
        this.userId = userId;
        this.role = role;
    }

    public User(
            String email,
            String password,
            String username,
            String nickname,
            UserRoleEnum role,
            Long kakaoId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
