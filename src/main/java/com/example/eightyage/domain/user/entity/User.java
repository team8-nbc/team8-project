package com.example.eightyage.domain.user.entity;

import com.example.eightyage.domain.user.userrole.UserRole;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Builder
    public User(Long id, String email, String nickname, String password, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return User.builder()
                .id(authUser.getUserId())
                .email(authUser.getEmail())
                .nickname(authUser.getNickname())
                .userRole(UserRole.of(authUser.getAuthorities().iterator().next().getAuthority()))
                .build();
    }

    public void deleteUser() {
        this.deletedAt = LocalDateTime.now();
    }
}
