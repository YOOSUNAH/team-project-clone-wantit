package io.dcns.wantitauction.global.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "refresh_token")
@Entity
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String token;

    protected RefreshTokenEntity(){

    }

    private RefreshTokenEntity(Long userId, String token){
        this.userId = userId;
        this.token = token;
    }

    public static RefreshTokenEntity of(Long userId, String token){
        return new RefreshTokenEntity(userId, token);
    }

}
