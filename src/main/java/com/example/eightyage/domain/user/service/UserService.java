package com.example.eightyage.domain.user.service;

import com.example.eightyage.domain.user.dto.request.UserDeleteRequestDto;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.userrole.UserRole;
import com.example.eightyage.domain.user.repository.UserRepository;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.eightyage.global.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원저장 */
    @Transactional
    public User saveUser(String email, String nickname, String password, String userRole) {

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(DUPLICATE_EMAIL.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(encodedPassword)
                .userRole(UserRole.of(userRole))
                .build();

        return userRepository.save(user);
    }

    /* 회원탈퇴 */
    @Transactional
    public void deleteUser(AuthUser authUser, UserDeleteRequestDto request) {
        User findUser = findUserByIdOrElseThrow(authUser.getUserId());

        if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword())) {
            throw new UnauthorizedException(INVALID_PASSWORD.getMessage());
        }

        findUser.deleteUser();
    }

    public User findUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException(USER_EMAIL_NOT_FOUND.getMessage())
        );
    }

    public User findUserByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_ID_NOT_FOUND.getMessage())
        );
    }
}
